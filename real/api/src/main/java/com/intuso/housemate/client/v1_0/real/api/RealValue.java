package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Value;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O,
        TYPE extends RealType<O, ?>,
        VALUE extends RealValue<O, TYPE, VALUE>>
        extends RealValueBase<Value.Data, O, TYPE, Value.Listener<? super VALUE>, VALUE>,
        Value<O,
        TYPE,
        VALUE> {}
