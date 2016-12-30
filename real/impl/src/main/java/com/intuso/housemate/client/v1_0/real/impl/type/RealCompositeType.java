package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.api.type.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealListGeneratedImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealSubTypeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Real type for types that are made up of a collection of other types. For example, a GPS position is made up of two
 * double sub types, one for latitude, one for longitude
 * @param <O> the type of the type's value
 */
public final class RealCompositeType<O>
        extends RealTypeImpl<O>
        implements SubType.Container<RealListGeneratedImpl<RealSubTypeImpl<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final TypeSerialiser<O> serialiser;
    private final RealListGeneratedImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param listenersFactory
     * @param subTypes the compound type's sub types
     */
    @Inject
    protected RealCompositeType(@Assisted Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                @Assisted Iterable<RealSubTypeImpl<?>> subTypes,
                                ListenersFactory listenersFactory,
                                RealListGeneratedImpl.Factory<RealSubTypeImpl<?>> subTypesFactory) {
        super(logger, new CompositeData(id, name, description), listenersFactory);
        this.serialiser = new Serialiser<>();
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, SUB_TYPES_ID),
                SUB_TYPES_ID,
                SUB_TYPES_NAME,
                SUB_TYPES_DESCRIPTION,
                subTypes);
    }

    @Override
    public O deserialise(Instance instance) {
        return serialiser.deserialise(instance);
    }

    @Override
    public Instance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        subTypes.init(ChildUtil.name(name, SUB_TYPES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public final RealListGeneratedImpl<RealSubTypeImpl<?>> getSubTypes() {
        return subTypes;
    }

    private class Serialiser<O> implements TypeSerialiser<O> {

        @Override
        public Instance serialise(O o) {
            return null; // todo build a map of subtype to value
        }

        @Override
        public O deserialise(Instance instance) {
            return null; // todo deserialise the map of subtype to value
        }
    }

    public interface Factory  {
        RealCompositeType create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    Iterable<RealSubTypeImpl<?>> options);
    }
}
