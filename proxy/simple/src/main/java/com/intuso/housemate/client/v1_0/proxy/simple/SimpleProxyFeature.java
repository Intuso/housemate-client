package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyFeature;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyFeature extends ProxyFeature<
        SimpleProxyList<CommandData, SimpleProxyCommand>,
        SimpleProxyList<ValueData, SimpleProxyValue>,
        SimpleProxyFeature> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyFeature(Logger logger,
                              ListenersFactory listenersFactory,
                              Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                              @Assisted FeatureData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
