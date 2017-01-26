package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyProperty;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyProperty extends ProxyProperty<
        SimpleProxyType,
        SimpleProxyCommand,
        SimpleProxyProperty> {

    @Inject
    public SimpleProxyProperty(@Assisted Logger logger,
                               ManagedCollectionFactory managedCollectionFactory,
                               ProxyObject.Factory<SimpleProxyCommand> commandFactory) {
        super(logger, managedCollectionFactory, commandFactory);
    }
}
