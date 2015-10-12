package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeature;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.Device;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            COMMANDS extends ProxyList<CommandData, COMMAND, COMMANDS>,
            VALUE extends ProxyValue<?, VALUE>,
            VALUES extends ProxyList<ValueData, VALUE, VALUES>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<PropertyData, PROPERTY, PROPERTIES>,
            FEATURE extends ProxyFeature<?, DEVICE>,
            DEVICE extends ProxyDevice<COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, FEATURE, DEVICE>>
        extends ProxyObject<DeviceData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, DEVICE, Device.Listener<? super DEVICE>>
        implements Device<COMMAND, COMMAND, COMMAND, COMMAND, COMMANDS, VALUE, VALUE, PROPERTY, VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyDevice(Log log, ListenersFactory listenersFactory, DeviceData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRenameCommand() {
        return (COMMAND) getChild(DeviceData.RENAME_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(DeviceData.REMOVE_ID);
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
        return (VALUE) getChild(DeviceData.RUNNING_ID);
    }

    @Override
    public COMMAND getStartCommand() {
        return (COMMAND) getChild(DeviceData.START_ID);
    }

    @Override
    public COMMAND getStopCommand() {
        return (COMMAND) getChild(DeviceData.STOP_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getValue() != null ? error.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return (VALUE) getChild(DeviceData.ERROR_ID);
    }

    @Override
    public PROPERTY getDriverProperty() {
        return (PROPERTY) getChild(DeviceData.DRIVER_ID);
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return (VALUE) getChild(DeviceData.DRIVER_LOADED_ID);
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
        result.add(addMessageListener(DeviceData.NEW_NAME, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                String oldName = getData().getName();
                String newName = message.getPayload().getValue();
                getData().setName(newName);
                for(Device.Listener<? super DEVICE> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        addChildLoadedListener(DeviceData.RUNNING_ID, new ChildLoadedListener<DEVICE, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(DEVICE object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getRunningValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Device.Listener<? super DEVICE> listener : getObjectListeners())
                            listener.running(getThis(), isRunning());
                    }
                }));
            }
        });
        addChildLoadedListener(DeviceData.ERROR_ID, new ChildLoadedListener<DEVICE, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(DEVICE object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Device.Listener<? super DEVICE> listener : getObjectListeners())
                            listener.error(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public final COMMANDS getCommands() {
        return (COMMANDS) getChild(DeviceData.COMMANDS_ID);
    }

    @Override
    public final VALUES getValues() {
        return (VALUES) getChild(DeviceData.VALUES_ID);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(DeviceData.PROPERTIES_ID);
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }

    public abstract <F extends FEATURE> F getFeature(String featureId);
}
