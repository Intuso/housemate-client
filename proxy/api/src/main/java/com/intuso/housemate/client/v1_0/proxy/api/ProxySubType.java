package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.object.v1_0.api.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<SUB_TYPE extends ProxySubType<SUB_TYPE>>
        extends ProxyObject<SubTypeData, NoChildrenData, NoChildrenProxyObject, SUB_TYPE, SubType.Listener<? super SUB_TYPE>>
        implements SubType<SUB_TYPE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySubType(Logger logger, ListenersFactory listenersFactory, SubTypeData data) {
        super(listenersFactory, logger, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }
}
