package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.System;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.ChildUtil;
import com.intuso.housemate.client.v1_0.proxy.ProxyFailable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 * @param <SYSTEM> the type of the device
 */
public abstract class ProxySystem<
        VALUE extends ProxyValue<?, VALUE>,
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        SYSTEM extends ProxySystem<VALUE, COMMAND, PROPERTIES, SYSTEM>>
        extends ProxyObject<System.Data, System.Listener<? super SYSTEM>>
        implements System<VALUE, COMMAND, PROPERTIES, SYSTEM>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final PROPERTIES playbackDevices;
    private final COMMAND addPlaybackDeviceCommand;
    private final PROPERTIES powerDevices;
    private final COMMAND addPowerDeviceCommand;
    private final PROPERTIES runDevices;
    private final COMMAND addRunDeviceCommand;
    private final PROPERTIES temperatureSensorDevices;
    private final COMMAND addTemperatureSensorDeviceCommand;
    private final PROPERTIES volumeDevices;
    private final COMMAND addVolumeDeviceCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxySystem(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<VALUE> valueFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<PROPERTIES> propertiesFactory) {
        super(logger, System.Data.class, managedCollectionFactory, receiverFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        playbackDevices = propertiesFactory.create(ChildUtil.logger(logger, PLAYBACK));
        addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK));
        powerDevices = propertiesFactory.create(ChildUtil.logger(logger, POWER));
        addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER));
        runDevices = propertiesFactory.create(ChildUtil.logger(logger, RUN));
        addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN));
        temperatureSensorDevices = propertiesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR));
        volumeDevices = propertiesFactory.create(ChildUtil.logger(logger, VOLUME));
        addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, RENAME_ID));
        removeCommand.init(ChildUtil.name(name, REMOVE_ID));
        errorValue.init(ChildUtil.name(name, ERROR_ID));
        playbackDevices.init(ChildUtil.name(name, PLAYBACK));
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK));
        powerDevices.init(ChildUtil.name(name, POWER));
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER));
        runDevices.init(ChildUtil.name(name, RUN));
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN));
        temperatureSensorDevices.init(ChildUtil.name(name, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDevices.init(ChildUtil.name(name, VOLUME));
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        playbackDevices.uninit();
        addPlaybackDeviceCommand.uninit();
        powerDevices.uninit();
        addPowerDeviceCommand.uninit();
        runDevices.uninit();
        addRunDeviceCommand.uninit();
        temperatureSensorDevices.uninit();
        addTemperatureSensorDeviceCommand.uninit();
        volumeDevices.uninit();
        addVolumeDeviceCommand.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
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

    @Override
    public PROPERTIES getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public COMMAND getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public PROPERTIES getPowerDevices() {
        return powerDevices;
    }

    @Override
    public COMMAND getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public PROPERTIES getRunDevices() {
        return runDevices;
    }

    @Override
    public COMMAND getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public PROPERTIES getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public COMMAND getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public PROPERTIES getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public COMMAND getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(REMOVE_ID.equals(id))
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
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:16
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxySystem<
            ProxyValue.Simple,
            ProxyCommand.Simple,
            ProxyList.Simple<ProxyProperty.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, valueFactory, commandFactory, propertiesFactory);
        }
    }
}
