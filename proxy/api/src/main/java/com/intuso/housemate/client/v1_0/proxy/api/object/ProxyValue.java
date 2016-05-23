package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValue<
            TYPE extends ProxyType<?>,
            VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<Value.Data, TYPE, Value.Listener<? super VALUE>, VALUE>
        implements Value<Type.Instances, TYPE, VALUE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValue(Logger logger, ListenersFactory listenersFactory) {
        super(logger, Value.Data.class, listenersFactory);
    }
}
