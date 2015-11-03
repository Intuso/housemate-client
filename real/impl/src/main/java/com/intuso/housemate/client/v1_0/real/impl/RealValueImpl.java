package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public class RealValueImpl<O> extends RealValueBaseImpl<ValueData, NoChildrenData, RealObject<NoChildrenData, ?, ?, ?>, O,
        Value.Listener<? super RealValue<O>>, RealValue<O>>
        implements RealValue<O> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                         RealType<O> type, O... values) {
        this(log, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                         RealType<O> type, List<O> values) {
        super(log, listenersFactory, new ValueData(id, name, description, type.getId(), RealTypeImpl.serialiseAll(type, values)), type);
    }
}
