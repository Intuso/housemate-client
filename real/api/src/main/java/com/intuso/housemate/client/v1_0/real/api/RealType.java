package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.Type;
import com.intuso.housemate.object.v1_0.api.TypeSerialiser;

/**
 * @param <O> the type of the type instances
 */
public interface RealType<O>
        extends Type<RealType<O>>, TypeSerialiser<O> {

    interface Container extends Type.Container<RealList<RealType<?>>> {
        void addType(RealType<?> type);
        void removeType(RealType<?> type);
    }
}
