package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealPropertyImpl<O>
        extends RealValueBaseImpl<O, Property.Data, Property.Listener<? super RealPropertyImpl<O>>, RealPropertyImpl<O>>
        implements RealProperty<O, RealTypeImpl<O>, RealCommandImpl, RealPropertyImpl<O>> {

    private RealCommandImpl setCommand;

    /**
     * @param logger {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param listenersFactory
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealPropertyImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, RealTypeImpl<O> type,
                            O... values) {
        this(logger, id, name, description, listenersFactory, type, Arrays.asList(values));
    }

    /**
     * @param logger {@inheritDoc}
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param listenersFactory
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealPropertyImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, RealTypeImpl<O> type,
                            List<O> values) {
        super(logger, new Property.Data(id, name, description), listenersFactory, type, values);
        setCommand = new RealCommandImpl(ChildUtil.logger(logger, Property.SET_COMMAND_ID), Property.SET_COMMAND_ID, Property.SET_COMMAND_ID,
                "The function to change the property's value", listenersFactory,
                new RealParameterImpl<>(ChildUtil.logger(logger, Property.SET_COMMAND_ID, Property.VALUE_PARAM), Property.VALUE_PARAM, Property.VALUE_PARAM, "The new value for the property", listenersFactory, type)) {
            @Override
            public void perform(Type.InstanceMap serialisedValues) {
                List<O> values = RealTypeImpl.deserialiseAll(getType(), serialisedValues.getChildren().get(Property.VALUE_PARAM));
                RealPropertyImpl.this.setValues(values);
            }
        };
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
    public void set(final O value, Command.PerformListener<? super RealCommandImpl> listener) {
        getSetCommand().perform(new Type.InstanceMap() {
            {
                getChildren().put(Property.VALUE_ID, RealTypeImpl.serialiseAll(getType(), value));
            }
        }, listener);
    }

    @Override
    public RealCommandImpl getSetCommand() {
        return setCommand;
    }
}
