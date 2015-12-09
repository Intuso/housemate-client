package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealSubTypeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.comms.v1_0.api.payload.CompoundTypeData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.object.v1_0.api.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Real type for types that are made up of a collection of other types. For example, a GPS position is made up of two
 * double sub types, one for latitude, one for longitude
 * @param <O> the type of the type's value
 */
public abstract class RealCompoundType<O>
        extends RealTypeImpl<CompoundTypeData, ListData<SubTypeData>, O>
        implements SubType.Container<RealListImpl<SubTypeData, RealSubTypeImpl<?>>> {

    public final static String SUB_TYPES_ID = "sub-types";
    public final static String SUB_TYPES_NAME = "Sub types";
    public final static String SUB_TYPES_DESCRIPTION = "The sub types that combine to form this type";

    private final RealListImpl<SubTypeData, RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, RealSubTypeImpl<?>... subTypes) {
        this(logger, listenersFactory, id, name, description, minValues, maxValues, Arrays.asList(subTypes));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the compound type's id
     * @param name the compound type's name
     * @param description the compound type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param subTypes the compound type's sub types
     */
    protected RealCompoundType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                               int maxValues, List<RealSubTypeImpl<?>> subTypes) {
        super(logger, listenersFactory, new CompoundTypeData(id, name, description, minValues, maxValues));
        this.subTypes = new RealListImpl<>(logger, listenersFactory, SUB_TYPES_ID, SUB_TYPES_NAME, SUB_TYPES_DESCRIPTION);
        addChild(this.subTypes);
        for(RealSubTypeImpl<?> subType : subTypes)
            this.subTypes.add(subType);
    }

    @Override
    public final RealListImpl<SubTypeData, RealSubTypeImpl<?>> getSubTypes() {
        return subTypes;
    }
}
