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

    /**
     * Creates a double value object
     * @param logger the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a double value object
     */
    public static RealValueImpl<Double> createValue(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                    Double value) {
        return new RealValueImpl<>(logger, id, name, description, listenersFactory, new DoubleType(listenersFactory), value);
    }

    /**
     * Creates a double property object
     * @param logger the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a double property object
     */
    public static RealPropertyImpl<Double> createProperty(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                          List<Double> values) {
        return new RealPropertyImpl<>(logger, id, name, description, listenersFactory, new DoubleType(listenersFactory), values);
    }

    /**
     * Creates a double parameter object
     * @param logger the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a double parameter object
     */
    public static RealParameterImpl<Double> createParameter(Logger logger, String id, String name, String description, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, id, name, description, listenersFactory, new DoubleType(listenersFactory));
    }
}
