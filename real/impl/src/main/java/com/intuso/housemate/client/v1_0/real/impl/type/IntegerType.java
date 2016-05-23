package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for an integer
 */
public class IntegerType extends RealSimpleType<Integer> {

    public final static TypeSerialiser<Integer> SERIALISER = new TypeSerialiser<Integer>() {
        @Override
        public Instance serialise(Integer i) {
            return i != null ? new Instance(i.toString()) : null;
        }

        @Override
        public Integer deserialise(Instance value) {
            return value != null && value.getValue() != null ? new Integer(value.getValue()) : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(IntegerType.class);

    @Inject
    public IntegerType(ListenersFactory listenersFactory) {
        super(logger, Type.Simple.Integer, SERIALISER, listenersFactory);
    }
}
