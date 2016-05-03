package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <ROOT> the type of the root
 */
public abstract class ProxyRoot<
            SERVERS extends ProxyList<? extends ProxyServer<?, ?, ?, ?, ?, ?>>,
            ROOT extends ProxyRoot<SERVERS, ROOT>>
        implements
            Server.Container<SERVERS> {

    public final static String SERVERS_ID = "servers";

    private final SERVERS servers;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyRoot(Logger logger,
                     ListenersFactory listenersFactory,
                     Connection connection,
                     ProxyObject.Factory<SERVERS> serversFactory) throws JMSException {
        servers = serversFactory.create(ChildUtil.logger(logger, SERVERS_ID));
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        servers.init(SERVERS_ID, session);
    }

    @Override
    public SERVERS getServers() {
        return servers;
    }
}
