package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyFeature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyFeature extends ProxyFeature<SimpleProxyCommand,
        SimpleProxyList<SimpleProxyCommand>,
        SimpleProxyValue,
        SimpleProxyList<SimpleProxyValue>,
        SimpleProxyProperty,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyFeature> {

    @Inject
    public SimpleProxyFeature(@Assisted Logger logger,
                              ListenersFactory listenersFactory,
                              Factory<SimpleProxyCommand> commandFactory,
                              Factory<SimpleProxyList<SimpleProxyCommand>> commandsFactory,
                              Factory<SimpleProxyValue> valueFactory,
                              Factory<SimpleProxyList<SimpleProxyValue>> valuesFactory,
                              Factory<SimpleProxyProperty> propertyFactory,
                              Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory) {
        super(logger, listenersFactory, commandFactory, commandsFactory, valueFactory, valuesFactory, propertyFactory, propertiesFactory);
    }
}
