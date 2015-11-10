package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.Value;

import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O>
        extends RealValueBase<O, Value.Listener<? super RealValue<O>>, RealValue<O>>,
        Value<TypeInstances, RealValue<O>> {

        interface Factory {
                RealValue<?> create(@Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    RealType<?> type,
                                    List<?> values);
        }
}
