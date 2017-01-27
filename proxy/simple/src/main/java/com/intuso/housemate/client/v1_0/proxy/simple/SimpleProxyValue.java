package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyValue;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyValue extends ProxyValue<SimpleProxyType, SimpleProxyValue> {

    @Inject
    public SimpleProxyValue(ManagedCollectionFactory managedCollectionFactory,
                            @Assisted Logger logger) {
        super(logger, managedCollectionFactory);
    }
}
