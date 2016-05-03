package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyDevice;
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
        SimpleProxyValue,
        SimpleProxyProperty,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyList<SimpleProxyFeature>,
        SimpleProxyDevice> {

    @Inject
    public SimpleProxyDevice(@Assisted Logger logger,
                             ListenersFactory listenersFactory,
                             Factory<SimpleProxyCommand> commandFactory,
                             Factory<SimpleProxyValue> valueFactory,
                             Factory<SimpleProxyProperty> propertyFactory,
                             Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory,
                             Factory<SimpleProxyList<SimpleProxyFeature>> featuresFactory) {
        super(logger, listenersFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory, featuresFactory);
    }
}
