package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.type.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Types;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanType extends RealSimpleType<Boolean> {

    public final static TypeSerialiser<Boolean> SERIALISER = new TypeSerialiser<Boolean>() {
        @Override
        public Instance serialise(Boolean b) {
            return b != null ? new Instance(b.toString()) : null;
        }

        @Override
        public Boolean deserialise(Instance value) {
            return value != null && value.getValue() != null ? Boolean.parseBoolean(value.getValue()) : null;
        }
    };

    @Inject
    public BooleanType(@Types Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Simple.Boolean.getId()), Type.Simple.Boolean, SERIALISER, listenersFactory);
    }
}
