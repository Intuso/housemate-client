package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ObjectTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyObjectType<
            TYPE extends ProxyObjectType<TYPE>>
        extends ProxyType<ObjectTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyObjectType(Logger logger, ListenersFactory listenersFactory, ObjectTypeData data) {
        super(logger, listenersFactory, data);
    }
}
