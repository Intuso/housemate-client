package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <O> the type of this value's value
 */
public final class RealValueImpl<O>
        extends RealValueBaseImpl<O, Value.Data, Value.Listener<? super RealValueImpl<O>>, RealValueImpl<O>>
        implements RealValue<O, RealTypeImpl<O>, RealValueImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param type the type of the value's value
     */
    @Inject
    public RealValueImpl(@Assisted Logger logger,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         @Assisted("min") int minValues,
                         @Assisted("max") int maxValues,
                         @Assisted Iterable<O> values,
                         ListenersFactory listenersFactory,
                         RealTypeImpl<O> type) {
        super(logger, new Value.Data(id, name, description, type.getId(), minValues, maxValues), listenersFactory, type, values);
    }

    public interface Factory<O> {
        RealValueImpl<O> create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                @Assisted("min") int minValues,
                                @Assisted("max") int maxValues,
                                Iterable<O> values);
    }
}
