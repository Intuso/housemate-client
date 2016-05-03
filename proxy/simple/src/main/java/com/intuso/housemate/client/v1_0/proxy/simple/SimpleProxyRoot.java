package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyRoot extends ProxyRoot<
        SimpleProxyList<SimpleProxyServer>,
        SimpleProxyRoot> {

    private final static Logger logger = LoggerFactory.getLogger(SimpleProxyRoot.class);

    @Inject
    public SimpleProxyRoot(ListenersFactory listenersFactory,
                           Connection connection,
                           ProxyObject.Factory<SimpleProxyList<SimpleProxyServer>> serversFactory) throws JMSException {
        super(logger, listenersFactory, connection, serversFactory);
    }
}
