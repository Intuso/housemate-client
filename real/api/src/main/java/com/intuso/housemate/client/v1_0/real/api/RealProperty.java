package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.view.PropertyView;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O,
        TYPE extends RealType<O, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTY extends RealProperty<O, TYPE, COMMAND, PROPERTY>>
        extends RealValueBase<Property.Data, O, TYPE, Property.Listener<? super PROPERTY>, PropertyView, PROPERTY>,
        Property<O, TYPE, COMMAND, PROPERTY> {}
