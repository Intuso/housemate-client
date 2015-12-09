package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValue<
            TYPE extends ProxyType<?, ?, ?, ?>,
            VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<ValueData, NoChildrenData, NoChildrenProxyObject, TYPE, Value.Listener<? super VALUE>, VALUE>
        implements Value<TypeInstances, VALUE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValue(Logger logger, ListenersFactory listenersFactory, ValueData data) {
        super(logger, listenersFactory, data);
    }
}
