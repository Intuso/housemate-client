package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealSubTypeImpl;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Type for a multiple selection of the values of an enum
 */
public abstract class EnumChoiceType<E extends Enum<E>> extends RealChoiceType<E> {

    private final TypeSerialiser<E> serialiser;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param values the values of the enum
     */
    protected EnumChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass, E... values) {
        this(logger, listenersFactory, id, name, description, minValues,
                maxValues,
                new EnumMap<E, List<RealSubTypeImpl<?>>>(enumClass), new EnumInstanceSerialiser<>(enumClass), values);
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param optionSubTypes the subtypes for each enum value
     * @param values the values of the enum
     */
    protected EnumChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass,
                             EnumMap<E, List<RealSubTypeImpl<?>>> optionSubTypes, E... values) {
        this(logger, listenersFactory, id, name, description, minValues, maxValues,
                optionSubTypes, new EnumInstanceSerialiser<>(enumClass), values);
    }
    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param elementSerialiser the serialiser for the enum elements
     * @param values the values of the enum
     */
    protected EnumChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass, TypeSerialiser<E> elementSerialiser, E... values) {
        this(logger, listenersFactory, id, name, description, minValues,
                maxValues, new EnumMap<E, List<RealSubTypeImpl<?>>>(enumClass), elementSerialiser, values);
    }
    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param optionSubTypes the subtypes for each enum value
     * @param elementSerialiser the serialiser for the enum elements
     * @param values the values of the enum
     */
    protected EnumChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, EnumMap<E, List<RealSubTypeImpl<?>>> optionSubTypes,
                             TypeSerialiser<E> elementSerialiser, E... values) {
        super(logger, listenersFactory, id, name, description, minValues,
                maxValues, convertValuesToOptions(logger, listenersFactory, values, optionSubTypes));
        this.serialiser = elementSerialiser;
    }

    @Override
    public TypeInstance serialise(E o) {
        return serialiser.serialise(o);
    }

    @Override
    public E deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }

    /**
     * Converts the values of an enum to option objects
     *
     * @param logger the log
     * @param listenersFactory
     *@param values the enum's values
     * @param optionSubTypes the subtypes for each enum value   @return a list of option objects, one for each value of the enum
     */
    private static <E extends Enum<E>> List<RealOptionImpl> convertValuesToOptions(final Logger logger, final ListenersFactory listenersFactory, E[] values,
                                                                               final EnumMap<E, List<RealSubTypeImpl<?>>> optionSubTypes) {
        return Lists.transform(Arrays.asList(values), new Function<E, RealOptionImpl>() {
            @Override
            public RealOptionImpl apply(E value) {
                if(optionSubTypes.containsKey(value))
                    return new RealOptionImpl(logger, listenersFactory, value.name(), value.name(), value.name(), optionSubTypes.get(value));
                else
                    return new RealOptionImpl(logger, listenersFactory, value.name(), value.name(), value.name());
            }
        });
    }

    /**
     * Serialiser for enum values
     * @param <E> the enum type
     */
    public static class EnumInstanceSerialiser<E extends Enum<E>> implements TypeSerialiser<E> {

        private final Class<E> enumClass;

        /**
         * @param enumClass the class of the enum
         */
        public EnumInstanceSerialiser(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public TypeInstance serialise(E value) {
            return value != null ? new TypeInstance(value.name()) : null;
        }

        @Override
        public E deserialise(TypeInstance value) {
            try {
                return value != null && value.getValue() != null ? Enum.valueOf(enumClass, value.getValue()) : null;
            } catch(Throwable t) {
                throw new HousemateCommsException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
            }
        }
    }
}
