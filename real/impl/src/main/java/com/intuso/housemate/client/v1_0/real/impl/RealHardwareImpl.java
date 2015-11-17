package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationProcessor;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;
import com.intuso.housemate.object.v1_0.api.Hardware;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for all hardwares
 */
public final class RealHardwareImpl<DRIVER extends HardwareDriver>
        extends RealObject<HardwareData, HousemateData<?>, RealObject<?, ?, ?, ?>, Hardware.Listener<? super RealHardware<DRIVER>>>
        implements RealHardware<DRIVER> {

    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealList<RealProperty<?>> properties;

    private final RemoveCallback removeCallback;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the hardware's data
     */
    @Inject
    public RealHardwareImpl(Log log,
                            ListenersFactory listenersFactory,
                            AnnotationProcessor annotationProcessor,
                            HardwareFactoryType driverFactoryType,
                            @Assisted HardwareData data,
                            @Assisted RemoveCallback removeCallback) {
        super(log, listenersFactory, data);
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(log, listenersFactory, HardwareData.RENAME_ID, HardwareData.RENAME_ID, "Rename the hardware", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, HardwareData.NAME_ID, HardwareData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(HardwareData.NAME_ID)) {
                    String newName = values.getChildren().get(HardwareData.NAME_ID).getFirstValue();
                    if (newName != null && !RealHardwareImpl.this.getName().equals(newName)) {
                        RealHardwareImpl.this.getData().setName(newName);
                        for(Hardware.Listener<? super RealHardwareImpl<DRIVER>> listener : RealHardwareImpl.this.getObjectListeners())
                            listener.renamed(RealHardwareImpl.this, RealHardwareImpl.this.getName(), newName);
                        RealHardwareImpl.this.sendMessage(HardwareData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.removeCommand = new RealCommandImpl(log, listenersFactory, HardwareData.REMOVE_ID, HardwareData.REMOVE_ID, "Remove the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while hardware is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(log, listenersFactory, HardwareData.RUNNING_ID, HardwareData.RUNNING_ID, "Whether the hardware is running or not", false);
        this.startCommand = new RealCommandImpl(log, listenersFactory, HardwareData.START_ID, HardwareData.START_ID, "Start the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setTypedValues(true);
                }
            }
        };
        this.stopCommand = new RealCommandImpl(log, listenersFactory, HardwareData.STOP_ID, HardwareData.STOP_ID, "Stop the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setTypedValues(false);
                }
            }
        };
        this.errorValue = StringType.createValue(log, listenersFactory, HardwareData.ERROR_ID, HardwareData.ERROR_ID, "Current error for the hardware", null);
        this.driverProperty = (RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>>) new RealPropertyImpl(log, listenersFactory, "driver", "Driver", "The hardware's driver", driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(log, listenersFactory, HardwareData.DRIVER_LOADED_ID, HardwareData.DRIVER_LOADED_ID, "Whether the hardware's driver is loaded or not", false);
        this.properties = (RealList)new RealListImpl<>(log, listenersFactory, HardwareData.PROPERTIES_ID, HardwareData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild((RealListImpl)properties);
        driverProperty.addObjectListener(new Property.Listener<RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<HardwareDriver.Factory<DRIVER>> driverFactory = driverProperty.getTypedValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(this);
                for(RealProperty<?> property : annotationProcessor.findProperties(driver))
                    properties.add(property);
                errorValue.setTypedValues((String) null);
                driverLoadedValue.setTypedValues(false);
                _start();
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            _stop();
            driverLoadedValue.setTypedValues(false);
            errorValue.setTypedValues("Driver not loaded");
            driver = null;
            for (RealProperty<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealCommand getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValue<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public boolean isDriverLoaded() {
        return driverLoadedValue.getTypedValue() != null ? driverLoadedValue.getTypedValue() : false;
    }

    @Override
    public RealCommand getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealCommand getStartCommand() {
        return startCommand;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return runningValue;
    }

    public boolean isRunning() {
        return runningValue.getTypedValue() != null ? runningValue.getTypedValue() : false;
    }

    protected final void remove() {
        removeCallback.removeHardware(this);
    }

    @Override
    public final RealList<RealProperty<?>> getProperties() {
        return properties;
    }

    protected final void _start() {
        try {
            // todo, call the driver
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start hardware: " + t.getMessage());
        }
    }

    protected final void _stop() {
        // todo, call the driver
    }

    @Override
    public void setError(String error) {
        errorValue.setTypedValues(error);
    }
}
