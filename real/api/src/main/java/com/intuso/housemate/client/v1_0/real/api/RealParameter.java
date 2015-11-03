package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.Parameter;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O>
        extends Parameter<RealParameter<O>> {
    RealType<O> getType();
}
