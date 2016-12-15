package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.SubType;

/**
 * @param <O> the type of the sub type's value
 */
public interface RealSubType<O,
        TYPE extends RealType<O, ?>,
        SUB_TYPE extends RealSubType<O, TYPE, SUB_TYPE>>
        extends SubType<TYPE, SUB_TYPE> {}
