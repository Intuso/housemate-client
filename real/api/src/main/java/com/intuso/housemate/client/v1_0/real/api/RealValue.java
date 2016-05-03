package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Value;
import org.slf4j.Logger;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O,
        TYPE extends RealType<O, ?>,
        VALUE extends RealValue<O, TYPE, VALUE>>
        extends RealValueBase<O, TYPE, Value.Listener<? super VALUE>, VALUE>,
        Value<O,
        TYPE,
        VALUE> {

    interface Factory<VALUE extends RealValue<?, ?, ?>> {
        VALUE create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?, ?> type);
    }
}
