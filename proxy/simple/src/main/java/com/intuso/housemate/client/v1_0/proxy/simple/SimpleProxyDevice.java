package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyDevice extends ProxyDevice<
        SimpleProxyCommand,
        SimpleProxyProperty,
        SimpleProxyValue,
        SimpleProxyList<PropertyData, SimpleProxyProperty>,
        SimpleProxyList<FeatureData, SimpleProxyFeature>,
        SimpleProxyDevice> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyDevice(Logger logger,
                             ListenersFactory listenersFactory,
                             Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             @Assisted DeviceData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
