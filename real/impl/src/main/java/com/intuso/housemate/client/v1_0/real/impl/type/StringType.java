package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for a string
 */
public class StringType extends RealSimpleType<String> {

    public final static TypeSerialiser<String> SERIALISER = new TypeSerialiser<String>() {
        @Override
        public Instance serialise(String s) {
            return s != null ? new Instance(s) : null;
        }

        @Override
        public String deserialise(Instance value) {
            return value != null ? value.getValue() : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(StringType.class);

    @Inject
    public StringType(ListenersFactory listenersFactory) {
        super(logger, Type.Simple.String, SERIALISER, listenersFactory);
    }
}
