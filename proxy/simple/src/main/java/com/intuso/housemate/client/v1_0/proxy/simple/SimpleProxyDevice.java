package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyDevice extends ProxyDevice<
        SimpleProxyCommand,
        SimpleProxyList<CommandData, SimpleProxyCommand>, SimpleProxyValue,
        SimpleProxyList<ValueData, SimpleProxyValue>,
        SimpleProxyProperty,
        SimpleProxyList<PropertyData, SimpleProxyProperty>,
            SimpleProxyFeature,
        SimpleProxyDevice> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;
    private final ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyDevice> featureFactory;

    @Inject
    public SimpleProxyDevice(Log log,
                             ListenersFactory listenersFactory,
                             Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyDevice> featureFactory, @Assisted DeviceData data) {
        super(log, listenersFactory, data);
        this.objectFactory = objectFactory;
        this.featureFactory = featureFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }

    @Override
    public <F extends SimpleProxyFeature> F getFeature(String featureId) {
        return featureFactory.getFeature(featureId, getThis());
    }
}
