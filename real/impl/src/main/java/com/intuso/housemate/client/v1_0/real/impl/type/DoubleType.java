package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for a double
 */
public class DoubleType extends RealSimpleType<Double> {

    public final static TypeSerialiser<Double> SERIALISER = new TypeSerialiser<Double>() {
        @Override
        public Instance serialise(Double d) {
            return d != null ? new Instance(d.toString()) : null;
        }

        @Override
        public Double deserialise(Instance value) {
            return value != null && value.getValue() != null ? new Double(value.getValue()) : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(DoubleType.class);

    @Inject
    public DoubleType(ListenersFactory listenersFactory) {
        super(logger, Type.Simple.Double, SERIALISER, listenersFactory);
    }
}
