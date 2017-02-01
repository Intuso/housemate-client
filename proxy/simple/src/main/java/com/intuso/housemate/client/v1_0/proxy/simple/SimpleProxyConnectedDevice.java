package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyConnectedDevice;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyConnectedDevice extends ProxyConnectedDevice<SimpleProxyCommand,
        SimpleProxyList<SimpleProxyCommand>,
        SimpleProxyList<SimpleProxyValue>,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyConnectedDevice> {

    @Inject
    public SimpleProxyConnectedDevice(@Assisted Logger logger,
                                      ManagedCollectionFactory managedCollectionFactory,
                                      Factory<SimpleProxyCommand> commandFactory,
                                      Factory<SimpleProxyList<SimpleProxyCommand>> commandsFactory,
                                      Factory<SimpleProxyList<SimpleProxyValue>> valuesFactory,
                                      Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory) {
        super(logger, managedCollectionFactory, commandFactory, commandsFactory, valuesFactory, propertiesFactory);
    }
}
