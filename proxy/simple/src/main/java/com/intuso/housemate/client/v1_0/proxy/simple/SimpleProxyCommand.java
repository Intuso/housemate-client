package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyCommand;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyCommand extends ProxyCommand<
        SimpleProxyValue,
        SimpleProxyList<SimpleProxyParameter>,
        SimpleProxyCommand> {

    @Inject
    public SimpleProxyCommand(@Assisted Logger logger,
                              ManagedCollectionFactory managedCollectionFactory,
                              ProxyObject.Factory<SimpleProxyValue> valueFactory,
                              ProxyObject.Factory<SimpleProxyList<SimpleProxyParameter>> parametersFactory) {
        super(logger, managedCollectionFactory, valueFactory, parametersFactory);
    }
}
