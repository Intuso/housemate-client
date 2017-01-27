package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <TYPE> the type of the type
 * @param <COMMAND> the type of the command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<TYPE extends ProxyType<?>,
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            PROPERTY extends ProxyProperty<TYPE, COMMAND, PROPERTY>>
        extends ProxyValueBase<Property.Data, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>
        implements Property<Type.Instances, TYPE, COMMAND, PROPERTY> {

    private final COMMAND setCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyProperty(Logger logger,
                         ManagedCollectionFactory managedCollectionFactory,
                         ProxyObject.Factory<COMMAND> commandFactory) {
        super(logger, Property.Data.class, managedCollectionFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        setCommand.init(ChildUtil.name(name, SET_COMMAND_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public COMMAND getSetCommand() {
        return setCommand;
    }

    @Override
    public void set(final Type.Instances value, Command.PerformListener<? super COMMAND> listener) {
        Type.InstanceMap values = new Type.InstanceMap();
        values.getChildren().put(VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(SET_COMMAND_ID.equals(id))
            return setCommand;
        return null;
    }
}
