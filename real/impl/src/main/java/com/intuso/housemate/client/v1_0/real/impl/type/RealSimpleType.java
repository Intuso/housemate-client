package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Base class for types that have a simple type, such as string, integer etc
 */
public class RealSimpleType<O> extends RealTypeImpl<O> {

    private final TypeSerialiser<O> serialiser;

    /**
     * @param logger the log
     * @param simpleType the type of the simple type
     * @param serialiser the serialiser for the type
     * @param listenersFactory
     */
    protected RealSimpleType(Logger logger, Type.Simple simpleType, TypeSerialiser<O> serialiser, ListenersFactory listenersFactory) {
        super(logger, new SimpleData(simpleType), listenersFactory);
        this.serialiser = serialiser;
    }

    @Override
    public Instance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public O deserialise(Instance value) {
        return serialiser.deserialise(value);
    }
}
