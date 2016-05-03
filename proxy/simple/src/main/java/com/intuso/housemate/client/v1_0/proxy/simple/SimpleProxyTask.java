package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyTask;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyTask extends ProxyTask<
        SimpleProxyCommand,
        SimpleProxyValue,
        SimpleProxyProperty,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyTask> {

    @Inject
    public SimpleProxyTask(@Assisted Logger logger,
                           ListenersFactory listenersFactory,
                           ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                           ProxyObject.Factory<SimpleProxyValue> valueFactory,
                           ProxyObject.Factory<SimpleProxyProperty> propertyFactory,
                           ProxyObject.Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory) {
        super(logger, listenersFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory);
    }
}
