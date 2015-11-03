package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.SubType;

/**
 * @param <O> the type of the sub type's value
 */
public interface RealSubType<O>
        extends SubType<RealSubType<O>> {
    RealType<O> getType();
}
