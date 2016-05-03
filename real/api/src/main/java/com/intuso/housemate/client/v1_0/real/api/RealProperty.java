package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Property;
import org.slf4j.Logger;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O,
        TYPE extends RealType<O, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTY extends RealProperty<O, TYPE, COMMAND, PROPERTY>>
        extends RealValueBase<O, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>,
        Property<O, TYPE, COMMAND, PROPERTY> {

    interface Factory<PROPERTY extends RealProperty<?, ?, ?, ?>> {
        PROPERTY create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?, ?> type);
    }
}
