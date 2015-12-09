package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Type for a string
 */
public class StringType extends RealSimpleType<String> {

    public final static TypeSerialiser<String> SERIALISER = new TypeSerialiser<String>() {
        @Override
        public TypeInstance serialise(String s) {
            return s != null ? new TypeInstance(s) : null;
        }

        @Override
        public String deserialise(TypeInstance value) {
            return value != null ? value.getValue() : null;
        }
    };

    /**
     * @param logger {@inheritDoc}
     */
    @Inject
    public StringType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory, SimpleTypeData.Type.String, SERIALISER);
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
    public static RealValueImpl<String> createValue(Logger logger, ListenersFactory listenersFactory,
                                                String id, String name, String description, String value) {
        return new RealValueImpl<>(logger, listenersFactory, id, name, description, new StringType(logger, listenersFactory), value);
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
    public static RealPropertyImpl<String> createProperty(Logger logger, ListenersFactory listenersFactory,
                                                      String id, String name, String description, List<String> values) {
        return new RealPropertyImpl<>(logger, listenersFactory, id, name, description, new StringType(logger, listenersFactory), values);
    }

    /**
     * Creates a string parameter object
     * @param logger the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a string parameter object
     */
    public static RealParameterImpl<String> createParameter(Logger logger, ListenersFactory listenersFactory,
                                                        String id, String name, String description) {
        return new RealParameterImpl<>(logger, listenersFactory, id, name, description, new StringType(logger, listenersFactory));
    }
}
