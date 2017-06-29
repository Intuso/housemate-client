package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.ConvertingList;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.ChildUtil;
import com.intuso.housemate.client.v1_0.proxy.ProxyFailable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.object.view.CommandView;
import com.intuso.housemate.client.v1_0.proxy.object.view.DeviceGroupView;
import com.intuso.housemate.client.v1_0.proxy.object.view.ValueView;
import com.intuso.housemate.client.v1_0.proxy.object.view.View;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDeviceGroup<
        COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUE extends ProxyValue<?, ?>,
        VALUES extends ProxyList<VALUE, ?>,
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?>,
        DEVICE_GROUP extends ProxyDeviceGroup<COMMAND, COMMANDS, VALUE, VALUES, DEVICE, DEVICE_GROUP>>
        extends ProxyDevice<Device.Group.Data, Device.Group.Listener<? super DEVICE_GROUP>, DeviceGroupView, COMMAND, COMMANDS, VALUES, DEVICE_GROUP>
        implements Device.Group<COMMAND, COMMAND, COMMAND, VALUE, COMMANDS, VALUES, ConvertingList<VALUE, DEVICE>, DEVICE_GROUP>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND> {

    private final Factory<COMMAND> commandFactory;
    private final Factory<VALUE> valueFactory;

    private final VALUES playbackDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> playbackDevices;
    private final COMMAND addPlaybackDeviceCommand;
    private final VALUES powerDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> powerDevices;
    private final COMMAND addPowerDeviceCommand;
    private final VALUES runDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> runDevices;
    private final COMMAND addRunDeviceCommand;
    private final VALUES temperatureSensorDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> temperatureSensorDevices;
    private final COMMAND addTemperatureSensorDeviceCommand;
    private final VALUES volumeDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> volumeDevices;
    private final COMMAND addVolumeDeviceCommand;

    private COMMAND removeCommand;
    private VALUE errorValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceGroup(Logger logger,
                            String name,
                            ManagedCollectionFactory managedCollectionFactory,
                            Receiver.Factory receiverFactory,
                            ProxyServer<?, ?, ?, ?, ?, ?, ?, ?, ?> server,
                            Factory<COMMAND> commandFactory,
                            Factory<COMMANDS> commandsFactory,
                            Factory<VALUE> valueFactory,
                            Factory<VALUES> valuesFactory) {
        super(logger, name, Group.Data.class, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valuesFactory);
        this.commandFactory = commandFactory;
        this.valueFactory = valueFactory;
        playbackDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, PLAYBACK), ChildUtil.name(name, PLAYBACK));
        playbackDevices = new ConvertingList<>(playbackDeviceReferences, server.<DEVICE>findConverter());
        addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK), ChildUtil.name(name, ADD_PLAYBACK));
        powerDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, POWER), ChildUtil.name(name, POWER));
        powerDevices = new ConvertingList<>(powerDeviceReferences, server.<DEVICE>findConverter());
        addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER), ChildUtil.name(name, ADD_POWER));
        runDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, RUN), ChildUtil.name(name, RUN));
        runDevices = new ConvertingList<>(runDeviceReferences, server.<DEVICE>findConverter());
        addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN), ChildUtil.name(name, ADD_RUN));
        temperatureSensorDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR), ChildUtil.name(name, TEMPERATURE_SENSOR));
        temperatureSensorDevices = new ConvertingList<>(temperatureSensorDeviceReferences, server.<DEVICE>findConverter());
        addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR), ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, VOLUME), ChildUtil.name(name, VOLUME));
        volumeDevices = new ConvertingList<>(volumeDeviceReferences, server.<DEVICE>findConverter());
        addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME), ChildUtil.name(name, ADD_VOLUME));
    }

    @Override
    public DeviceGroupView createView() {
        return new DeviceGroupView();
    }

    @Override
    public void view(DeviceGroupView view) {

        super.view(view);

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if (errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                break;
            case SELECTION:
                if(removeCommand == null && view.getRemoveCommandView() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if (errorValue == null && view.getErrorValueView() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                removeCommand.view(new CommandView(View.Mode.ANCESTORS));
                errorValue.view(new ValueView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if (view.getRemoveCommandView() != null)
                    removeCommand.view(view.getRemoveCommandView());
                if (view.getErrorValueView() != null)
                    errorValue.view(view.getErrorValueView());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(removeCommand != null)
            removeCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        playbackDeviceReferences.uninit();
        addPlaybackDeviceCommand.uninit();
        powerDeviceReferences.uninit();
        addPowerDeviceCommand.uninit();
        runDeviceReferences.uninit();
        addRunDeviceCommand.uninit();
        temperatureSensorDeviceReferences.uninit();
        addTemperatureSensorDeviceCommand.uninit();
        volumeDeviceReferences.uninit();
        addVolumeDeviceCommand.uninit();
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    public VALUES getPlaybackDeviceReferences() {
        return playbackDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public COMMAND getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    public VALUES getPowerDeviceReferences() {
        return powerDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public COMMAND getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    public VALUES getRunDeviceReferences() {
        return runDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getRunDevices() {
        return runDevices;
    }

    @Override
    public COMMAND getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    public VALUES getTemperatureSensorDeviceReferences() {
        return temperatureSensorDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public COMMAND getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    public VALUES getVolumeDeviceReferences() {
        return volumeDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public COMMAND getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public Object<?, ?> getChild(String id) {
        if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(ERROR_ID.equals(id))
            return errorValue;
        else if(PLAYBACK.equals(id))
            return playbackDevices;
        else if(ADD_PLAYBACK.equals(id))
            return addPlaybackDeviceCommand;
        else if(POWER.equals(id))
            return powerDevices;
        else if(ADD_POWER.equals(id))
            return addPowerDeviceCommand;
        else if(RUN.equals(id))
            return runDevices;
        else if(ADD_RUN.equals(id))
            return addRunDeviceCommand;
        else if(TEMPERATURE_SENSOR.equals(id))
            return temperatureSensorDevices;
        else if(ADD_TEMPERATURE_SENSOR.equals(id))
            return addTemperatureSensorDeviceCommand;
        else if(VOLUME.equals(id))
            return volumeDevices;
        else if(ADD_VOLUME.equals(id))
            return addVolumeDeviceCommand;
        return super.getChild(id);
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyDeviceGroup<
            ProxyCommand.Simple,
            ProxyList.Simple<ProxyCommand.Simple>,
            ProxyValue.Simple,
            ProxyList.Simple<ProxyValue.Simple>,
            ProxyDevice<?, ?, ?, ?, ?, ?, ?>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      ProxyServer.Simple server,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, server, commandFactory, commandsFactory, valueFactory, valuesFactory);
        }
    }
}
