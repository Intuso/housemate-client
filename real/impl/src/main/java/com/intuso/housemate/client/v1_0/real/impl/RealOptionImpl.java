package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.real.api.RealOption;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Arrays;
import java.util.List;

public final class RealOptionImpl
        extends RealObject<Option.Data, Option.Listener<? super RealOptionImpl>>
        implements RealOption<RealListImpl<RealSubTypeImpl<?>>, RealOptionImpl> {

    private final RealListImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealSubTypeImpl<?>... subTypes) {
        this(logger, listenersFactory, id, name, description, Arrays.asList(subTypes));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOptionImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, List<RealSubTypeImpl<?>> subTypes) {
        super(logger, new Option.Data(id, name,  description), listenersFactory);
        this.subTypes = new RealListImpl<>(ChildUtil.logger(logger, Option.SUB_TYPES_ID),
                new com.intuso.housemate.client.v1_0.api.object.List.Data(Option.SUB_TYPES_ID, "Sub Types", "The sub types of this option"),
                listenersFactory,
                subTypes);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        subTypes.init(ChildUtil.name(name, Option.SUB_TYPES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public final RealListImpl<RealSubTypeImpl<?>> getSubTypes() {
        return subTypes;
    }
}
