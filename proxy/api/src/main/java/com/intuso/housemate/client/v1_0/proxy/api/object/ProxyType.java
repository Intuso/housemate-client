package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<TYPE extends ProxyType<TYPE>>
        extends ProxyObject<Type.Data, Type.Listener<? super TYPE>>
        implements Type<TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, Type.Data.class, listenersFactory);
    }
}
