package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;
import com.intuso.housemate.object.v1_0.api.Hardware;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyHardware<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            HARDWARE extends ProxyHardware<COMMAND, VALUE, PROPERTY, PROPERTIES, HARDWARE>>
        extends ProxyObject<HardwareData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, HARDWARE, Hardware.Listener<? super HARDWARE>>
        implements Hardware<COMMAND, COMMAND, COMMAND, VALUE, VALUE, PROPERTY, VALUE, PROPERTIES, HARDWARE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyHardware(Log log, ListenersFactory listenersFactory, HardwareData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRenameCommand() {
        return (COMMAND) getChild(HardwareData.RENAME_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(HardwareData.REMOVE_ID);
    }

    @Override
    public final boolean isRunning() {
        VALUE running = getRunningValue();
        return running.getValue() != null
                && running.getValue().getFirstValue() != null
                && Boolean.parseBoolean(running.getValue().getFirstValue());
    }

    @Override
    public VALUE getRunningValue() {
        return (VALUE) getChild(HardwareData.RUNNING_ID);
    }

    @Override
    public COMMAND getStartCommand() {
        return (COMMAND) getChild(HardwareData.START_ID);
    }

    @Override
    public COMMAND getStopCommand() {
        return (COMMAND) getChild(HardwareData.STOP_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getValue() != null ? error.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return (VALUE) getChild(HardwareData.ERROR_ID);
    }

    @Override
    public PROPERTY getDriverProperty() {
        return (PROPERTY) getChild(HardwareData.DRIVER_ID);
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return (VALUE) getChild(HardwareData.DRIVER_LOADED_ID);
    }

    @Override
    public final boolean isDriverLoaded() {
        VALUE driverLoaded = getDriverLoadedValue();
        return driverLoaded.getValue() != null
                && driverLoaded.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoaded.getValue().getFirstValue());
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(HardwareData.NEW_NAME, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                String oldName = getData().getName();
                String newName = message.getPayload().getValue();
                getData().setName(newName);
                for(Hardware.Listener<? super HARDWARE> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        addChildLoadedListener(HardwareData.RUNNING_ID, new ChildLoadedListener<HARDWARE, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(HARDWARE object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getRunningValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Hardware.Listener<? super HARDWARE> listener : getObjectListeners())
                            listener.running(getThis(), isRunning());
                    }
                }));
            }
        });
        addChildLoadedListener(HardwareData.ERROR_ID, new ChildLoadedListener<HARDWARE, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(HARDWARE object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Hardware.Listener<? super HARDWARE> listener : getObjectListeners())
                            listener.error(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(HardwareData.PROPERTIES_ID);
    }
}
