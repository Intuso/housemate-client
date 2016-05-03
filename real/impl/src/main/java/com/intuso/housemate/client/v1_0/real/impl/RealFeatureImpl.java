package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.api.object.Feature;
import com.intuso.housemate.client.v1_0.real.api.RealFeature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

public final class RealFeatureImpl
        extends RealObject<Feature.Data, Feature.Listener<? super RealFeatureImpl>>
        implements RealFeature<RealListImpl<RealCommandImpl>, RealListImpl<RealValueImpl<?>>, RealFeatureImpl> {

    private final RealListImpl<RealCommandImpl> commands;
    private final RealListImpl<RealValueImpl<?>> values;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the feature's id
     * @param name the feature's name
     * @param description the feature's description
     */
    public RealFeatureImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description) {
        super(logger, new Feature.Data(id, name,  description), listenersFactory);
        this.commands = new RealListImpl<>(ChildUtil.logger(logger, Feature.COMMANDS_ID),
                new com.intuso.housemate.client.v1_0.api.object.List.Data(Feature.COMMANDS_ID, "Commands", "The commands of this feature"),
                listenersFactory);
        this.values = new RealListImpl<>(ChildUtil.logger(logger, Feature.VALUES_ID),
                new com.intuso.housemate.client.v1_0.api.object.List.Data(Feature.VALUES_ID, "Values", "The values of this feature"),
                listenersFactory);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        commands.init(ChildUtil.name(name, Feature.COMMANDS_ID), session);
        values.init(ChildUtil.name(name, Feature.VALUES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public final RealListImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListImpl<RealValueImpl<?>> getValues() {
        return values;
    }
}
