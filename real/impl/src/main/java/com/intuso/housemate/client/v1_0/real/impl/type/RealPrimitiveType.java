package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base class for types that have a simple type, such as string, integer etc
 */
public class RealPrimitiveType<O> extends RealTypeImpl<O> {

    private final TypeSerialiser<O> serialiser;

    /**
     * @param logger the log
     * @param data
     * @param serialiser the serialiser for the type
     * @param managedCollectionFactory
     */
    protected RealPrimitiveType(Logger logger,
                                PrimitiveData data,
                                TypeSerialiser<O> serialiser,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, data, managedCollectionFactory);
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
