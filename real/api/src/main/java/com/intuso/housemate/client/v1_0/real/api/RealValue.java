package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.Value;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O>
        extends RealValueBase<O, Value.Listener<? super RealValue<O>>, RealValue<O>>,
        Value<TypeInstances, RealValue<O>> {}
