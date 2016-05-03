package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public final class RealValueImpl<O>
        extends RealValueBaseImpl<O, Value.Data, Value.Listener<? super RealValueImpl<O>>, RealValueImpl<O>>
        implements RealValue<O, RealTypeImpl<O>, RealValueImpl<O>> {

    /**
     * @param listenersFactory
     * @param logger {@inheritDoc}
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory,
                         RealTypeImpl<O> type, O... values) {
        this(logger, id, name, description, listenersFactory, type, Arrays.asList(values));
    }

    /**
     * @param logger {@inheritDoc}
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param listenersFactory
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    @Inject
    public RealValueImpl(@Assisted final Logger logger,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         ListenersFactory listenersFactory,
                         @Assisted RealTypeImpl<O> type,
                         @Nullable @Assisted List<O> values) {
        super(logger, new Value.Data(id, name, description), listenersFactory, type, values);
    }
}
