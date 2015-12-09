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
 * Type for an integer
 */
public class IntegerType extends RealSimpleType<Integer> {

    public final static TypeSerialiser<Integer> SERIALISER = new TypeSerialiser<Integer>() {
        @Override
        public TypeInstance serialise(Integer i) {
            return i != null ? new TypeInstance(i.toString()) : null;
        }

        @Override
        public Integer deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? new Integer(value.getValue()) : null;
        }
    };

    /**
     * @param logger {@inheritDoc}
     */
    @Inject
    public IntegerType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory, SimpleTypeData.Type.Integer, SERIALISER);
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
    public static RealValueImpl<Integer> createValue(Logger logger, ListenersFactory listenersFactory,
                                                 String id, String name, String description, Integer value) {
        return new RealValueImpl<>(logger, listenersFactory, id, name, description, new IntegerType(logger, listenersFactory), value);
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
    public static RealPropertyImpl<Integer> createProperty(Logger logger, ListenersFactory listenersFactory,
                                                       String id, String name, String description, List<Integer> values) {
        return new RealPropertyImpl<>(logger, listenersFactory, id, name, description, new IntegerType(logger, listenersFactory), values);
    }

    /**
     * Creates an integer parameter object
     * @param logger the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return an integer parameter object
     */
    public static RealParameterImpl<Integer> createParameter(Logger logger, ListenersFactory listenersFactory,
                                                         String id, String name, String description) {
        return new RealParameterImpl<>(logger, listenersFactory, id, name, description, new IntegerType(logger, listenersFactory));
    }
}
