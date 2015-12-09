package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyValue;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyValue extends ProxyValue<SimpleProxyType, SimpleProxyValue> {

    @Inject
    public SimpleProxyValue(Logger logger,
                            ListenersFactory listenersFactory,
                            @Assisted ValueData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
