package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import org.slf4j.Logger;

/**
 * @param <O> the type of the sub type's value
 */
public interface RealSubType<O,
        TYPE extends RealType<O, ?>,
        SUB_TYPE extends RealSubType<O, TYPE, SUB_TYPE>>
        extends SubType<TYPE, SUB_TYPE> {

    TYPE getType();

    interface Factory<SUB_TYPE extends RealSubType<?, ?, ?>> {
        SUB_TYPE create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?, ?> type);
    }
}
