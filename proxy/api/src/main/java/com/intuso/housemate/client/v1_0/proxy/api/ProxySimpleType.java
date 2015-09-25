package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxySimpleType<
            TYPE extends ProxySimpleType<TYPE>>
        extends ProxyType<SimpleTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySimpleType(Log log, ListenersFactory listenersFactory, SimpleTypeData data) {
        super(log, listenersFactory, data);
    }

    /**
     * Gets the simple type enum value of this type
     * @return the simple type enum value of this type
     */
    public SimpleTypeData.Type getType() {
        return getData().getType();
    }
}
