package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.real.api.RealSubType;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.object.v1_0.api.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <O> the type of the sub type's value
 */
public class RealSubTypeImpl<O>
        extends RealObject<SubTypeData, NoChildrenData, RealObject<NoChildrenData, ? ,?, ?>, SubType.Listener<? super RealSubType<O>>>
        implements RealSubType<O> {

    private final RealListImpl<TypeData<?>, RealTypeImpl<?, ?, ?>> types;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the sub type's id
     * @param name the sub type's name
     * @param description the sub type's description
     * @param types the types in the system
     */
    public RealSubTypeImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description, String typeId,
                           RealListImpl<TypeData<?>, RealTypeImpl<?, ?, ?>> types) {
        super(log, listenersFactory, new SubTypeData(id, name, description, typeId));
        this.types = types;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public final RealType<O> getType() {
        return (RealType<O>) types.get(getData().getType());
    }
}
