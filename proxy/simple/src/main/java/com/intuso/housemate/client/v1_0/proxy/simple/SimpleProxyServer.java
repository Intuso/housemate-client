package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.Server;
import com.intuso.utilities.collection.ManagedCollectionFactory;
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
        SimpleProxyList<SimpleProxySystem>,
        SimpleProxyList<SimpleProxyUser>,
        SimpleProxyList<SimpleProxyNode>,
        SimpleProxyServer> {

    @Inject
    public SimpleProxyServer(@Server Logger logger,
                             Connection connection,
                             ManagedCollectionFactory managedCollectionFactory,
                             Factory<SimpleProxyCommand> commandFactory,
                             Factory<SimpleProxyList<SimpleProxyAutomation>> automationsFactory,
                             Factory<SimpleProxyList<SimpleProxySystem>> devicesFactory,
                             Factory<SimpleProxyList<SimpleProxyUser>> usersFactory,
                             Factory<SimpleProxyList<SimpleProxyNode>> nodesFactory) {
        super(connection, logger, managedCollectionFactory, commandFactory, automationsFactory, devicesFactory, usersFactory, nodesFactory);
    }
}
