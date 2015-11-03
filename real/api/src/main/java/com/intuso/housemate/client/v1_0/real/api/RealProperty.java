package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.housemate.object.v1_0.api.TypeInstances;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O>
        extends RealValueBase<O, Property.Listener<? super RealProperty<O>>, RealProperty<O>>,
        Property<TypeInstances, RealCommand, RealProperty<O>> {}
