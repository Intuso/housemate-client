package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyList<ELEMENT extends ProxyObject<?, ?>> extends ProxyList<ELEMENT> {

    @Inject
    public SimpleProxyList(@Assisted Logger logger,
                           ListenersFactory listenersFactory,
                           ProxyObject.Factory<ELEMENT> elementFactory) {
        super(logger, listenersFactory, elementFactory);
    }
}
