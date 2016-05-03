package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Parameter;
import org.slf4j.Logger;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O,
        TYPE extends RealType<O, ?>,
        PARAMETER extends RealParameter<O, TYPE, PARAMETER>>
        extends Parameter<TYPE, PARAMETER> {

    TYPE getType();

    interface Factory<PARAMETER extends RealParameter<?, ?, ?>> {
        PARAMETER create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?, ?> type);
    }
}
