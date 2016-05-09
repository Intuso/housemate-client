package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Property;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O,
        TYPE extends RealType<O, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTY extends RealProperty<O, TYPE, COMMAND, PROPERTY>>
        extends RealValueBase<O, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>,
        Property<O, TYPE, COMMAND, PROPERTY> {}
