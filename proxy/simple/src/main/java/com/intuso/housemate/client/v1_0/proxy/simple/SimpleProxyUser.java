package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyUser;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyUser extends ProxyUser<
        SimpleProxyCommand,
        SimpleProxyProperty,
        SimpleProxyUser> {

    @Inject
    public SimpleProxyUser(@Assisted Logger logger,
                           ManagedCollectionFactory managedCollectionFactory,
                           ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                           ProxyObject.Factory<SimpleProxyProperty> propertyFactory) {
        super(logger, managedCollectionFactory, commandFactory, propertyFactory);
    }
}
