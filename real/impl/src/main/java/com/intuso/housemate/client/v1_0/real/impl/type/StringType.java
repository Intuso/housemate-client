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

    /**
     * Creates an string value object
     * @param logger the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a string value object
     */
    public static RealValueImpl<String> createValue(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                    String value) {
        return new RealValueImpl<>(logger, id, name, description, listenersFactory, new StringType(listenersFactory), value);
    }

    /**
     * Creates a string property object
     * @param logger the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a string property object
     */
    public static RealPropertyImpl<String> createProperty(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                                                          List<String> values) {
        return new RealPropertyImpl<>(logger, id, name, description, listenersFactory, new StringType(listenersFactory), values);
    }

    /**
     * Creates a string parameter object
     * @param logger the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a string parameter object
     */
    public static RealParameterImpl<String> createParameter(Logger logger, String id, String name, String description, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, id, name, description, listenersFactory, new StringType(listenersFactory));
    }
}
