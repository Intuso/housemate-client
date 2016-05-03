package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    /**
     * Creates an integer value object
     * @param logger the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return an integer value object
     */
    public static RealValueImpl<Integer> createValue(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                     Integer value) {
        return new RealValueImpl<>(logger, id, name, description, listenersFactory, new IntegerType(listenersFactory), value);
    }

    /**
     * Creates an integer property object
     * @param logger the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return an integer property object
     */
    public static RealPropertyImpl<Integer> createProperty(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                           List<Integer> values) {
        return new RealPropertyImpl<>(logger, id, name, description, listenersFactory, new IntegerType(listenersFactory), values);
    }

    /**
     * Creates an integer parameter object
     * @param logger the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return an integer parameter object
     */
    public static RealParameterImpl<Integer> createParameter(Logger logger, String id, String name, String description, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, id, name, description, listenersFactory, new IntegerType(listenersFactory));
    }
}
