package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.Root;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyServer extends ProxyServer<
        SimpleProxyCommand,
        SimpleProxyList<SimpleProxyAutomation>,
        SimpleProxyList<SimpleProxyDevice>,
        SimpleProxyList<SimpleProxyUser>,
        SimpleProxyList<SimpleProxyNode>,
        SimpleProxyServer> {

    @Inject
    public SimpleProxyServer(@Root Logger logger,
                             ListenersFactory listenersFactory,
                             ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyAutomation>> automationsFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyDevice>> devicesFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyUser>> usersFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyNode>> nodesFactory,
                             Connection connection) {
        super(logger, listenersFactory, commandFactory, automationsFactory, devicesFactory, usersFactory, nodesFactory, connection);
    }
}
