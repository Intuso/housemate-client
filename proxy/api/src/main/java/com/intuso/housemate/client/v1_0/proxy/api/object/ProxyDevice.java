package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.proxy.api.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base interface for all proxy features
 * @param <FEATURE> the feature type
 */
public abstract class ProxyDevice<COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        FEATURE extends ProxyDevice<COMMAND, COMMANDS, VALUES, PROPERTIES, FEATURE>>
        extends ProxyObject<Device.Data, Device.Listener<? super FEATURE>>
        implements Device<COMMAND, COMMANDS, VALUES, PROPERTIES, FEATURE>,
        ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final COMMANDS commands;
    private final VALUES values;
    private final PROPERTIES properties;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDevice(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<COMMANDS> commandsFactory,
                       Factory<VALUES> valuesFactory,
                       Factory<PROPERTIES> propertiesFactory) {
        super(logger, Device.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), connection);
        commands.init(ChildUtil.name(name, COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, VALUES_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final COMMANDS getCommands() {
        return commands;
    }

    @Override
    public final VALUES getValues() {
        return values;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(PROPERTIES_ID.equals(id))
            return properties;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}
