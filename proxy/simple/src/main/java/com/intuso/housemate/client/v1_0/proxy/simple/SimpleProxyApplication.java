package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyApplication;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationData;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyApplication extends ProxyApplication<
        SimpleProxyValue,
        SimpleProxyCommand,
        SimpleProxyApplicationInstance,
        SimpleProxyList<ApplicationInstanceData, SimpleProxyApplicationInstance>,
        SimpleProxyApplication> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyApplication(Logger logger,
                                  ListenersFactory listenersFactory,
                                  ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                                  @Assisted ApplicationData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
