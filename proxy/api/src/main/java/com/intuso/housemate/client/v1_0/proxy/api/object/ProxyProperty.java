package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

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
                         ListenersFactory listenersFactory,
                         ProxyObject.Factory<COMMAND> commandFactory) {
        super(logger, Property.Data.class, listenersFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        setCommand.init(ChildUtil.name(name, Property.SET_COMMAND_ID), session);
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
        values.getChildren().put(Property.VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }
}
