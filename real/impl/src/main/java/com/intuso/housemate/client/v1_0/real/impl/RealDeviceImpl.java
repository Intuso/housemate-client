package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationProcessor;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;
import com.intuso.housemate.object.v1_0.api.Device;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for all devices
 */
public final class RealDeviceImpl<DRIVER extends DeviceDriver>
        extends RealObject<
        DeviceData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Device.Listener<? super RealDevice<DRIVER>>>
        implements RealDevice<DRIVER> {

    private final static String PROPERTIES_DESCRIPTION = "The feature's properties";
    private final static String FEATURES_DESCRIPTION = "The device's features";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealList<RealProperty<?>> properties;
    private final RealList<RealFeature> features;

    private final RemoveCallback removeCallback;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the device's data
     */
    @Inject
    public RealDeviceImpl(Log log,
                          ListenersFactory listenersFactory,
                          AnnotationProcessor annotationProcessor,
                          DeviceFactoryType driverFactoryType,
                          @Assisted DeviceData data,
                          @Assisted RemoveCallback removeCallback) {
        super(log, listenersFactory, new DeviceData(data.getId(), data.getName(), data.getDescription()));
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(log, listenersFactory, DeviceData.RENAME_ID, DeviceData.RENAME_ID, "Rename the device", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, DeviceData.NAME_ID, DeviceData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(DeviceData.NAME_ID)) {
                    String newName = values.getChildren().get(DeviceData.NAME_ID).getFirstValue();
                    if (newName != null && !RealDeviceImpl.this.getName().equals(newName)) {
                        RealDeviceImpl.this.getData().setName(newName);
                        for(Device.Listener<? super RealDeviceImpl<DRIVER>> listener : RealDeviceImpl.this.getObjectListeners())
                            listener.renamed(RealDeviceImpl.this, RealDeviceImpl.this.getName(), newName);
                        RealDeviceImpl.this.sendMessage(DeviceData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.removeCommand = new RealCommandImpl(log, listenersFactory, DeviceData.REMOVE_ID, DeviceData.REMOVE_ID, "Remove the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while device is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(log, listenersFactory, DeviceData.RUNNING_ID, DeviceData.RUNNING_ID, "Whether the device is running or not", false);
        this.startCommand = new RealCommandImpl(log, listenersFactory, DeviceData.START_ID, DeviceData.START_ID, "Start the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setTypedValues(true);
                }
            }
        };
        this.stopCommand = new RealCommandImpl(log, listenersFactory, DeviceData.STOP_ID, DeviceData.STOP_ID, "Stop the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setTypedValues(false);
                }
            }
        };
        this.errorValue = StringType.createValue(log, listenersFactory, DeviceData.ERROR_ID, DeviceData.ERROR_ID, "Current error for the device", null);
        this.driverProperty = (RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>>) new RealPropertyImpl(log, listenersFactory, "driver", "Driver", "The device's driver", driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(log, listenersFactory, DeviceData.DRIVER_LOADED_ID, DeviceData.DRIVER_LOADED_ID, "Whether the device's driver is loaded or not", false);
        this.properties = (RealList)new RealListImpl<>(log, listenersFactory, DeviceData.PROPERTIES_ID, DeviceData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        this.features = (RealList)new RealListImpl<>(log, listenersFactory, DeviceData.FEATURES_ID, DeviceData.FEATURES_ID, FEATURES_DESCRIPTION);
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild((RealListImpl) properties);
        addChild((RealListImpl) features);
        driverProperty.addObjectListener(new Property.Listener<RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<DeviceDriver.Factory<DRIVER>> driverFactory = driverProperty.getTypedValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(this);
                for(RealFeature feature : annotationProcessor.findFeatures(driver))
                    features.add(feature);
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
            for (RealFeature feature : Lists.newArrayList(features))
                features.remove(feature.getId());
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
    public RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> getDriverProperty() {
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

    @Override
    public final RealList<RealProperty<?>> getProperties() {
        return properties;
    }

    @Override
    public final RealList<RealFeature> getFeatures() {
        return features;
    }

    protected final void remove() {
        removeCallback.removeDevice(this);
    }

    protected final void _start() {
        try {
            if(driver != null)
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(driver != null)
            driver.stop();
    }

    @Override
    public void setError(String error) {
        errorValue.setTypedValues(error);
    }
}
