package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.object.v1_0.api.Parameter;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O>
        extends Parameter<RealParameter<O>> {

    RealType<O> getType();

    interface Factory {
        RealParameter<?> create(@Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?> type);
    }
}
