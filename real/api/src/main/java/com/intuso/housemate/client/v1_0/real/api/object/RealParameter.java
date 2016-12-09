package com.intuso.housemate.client.v1_0.real.api.object;

import com.intuso.housemate.client.v1_0.api.object.Parameter;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O,
        TYPE extends RealType<O, ?>,
        PARAMETER extends RealParameter<O, TYPE, PARAMETER>>
        extends Parameter<TYPE, PARAMETER> {}
