package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 * @param <O> the type of the type instances
 */
public interface RealType<O, TYPE extends RealType<O, TYPE>>
        extends Type<TYPE>, TypeSerialiser<O> {

    interface Container<TYPE extends RealType<?, ?>, TYPES extends RealList<? extends TYPE>> extends Type.Container<TYPES> {
        void addType(TYPE type);
        void removeType(TYPE type);
    }
}
