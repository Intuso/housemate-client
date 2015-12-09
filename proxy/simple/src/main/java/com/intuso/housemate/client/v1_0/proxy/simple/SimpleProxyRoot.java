package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ServerData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyRoot extends ProxyRoot<
        SimpleProxyServer, SimpleProxyList<ServerData, SimpleProxyServer>,
        SimpleProxyRoot> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyRoot(Logger logger,
                           ListenersFactory listenersFactory,
                           PropertyRepository properties,
                           ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                           Router<?> router) {
        super(logger, listenersFactory, properties, router);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
