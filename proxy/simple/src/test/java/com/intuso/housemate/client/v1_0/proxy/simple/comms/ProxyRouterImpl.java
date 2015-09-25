package com.intuso.housemate.client.v1_0.proxy.simple.comms;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.simple.TestRealRoot;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class ProxyRouterImpl extends Router {

    private TestRealRoot realRoot;

    @Inject
    public ProxyRouterImpl(Log log, ListenersFactory listenersFactory, PropertyRepository properties) {
        super(log, listenersFactory, properties);
        connect();
        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
        register(TestEnvironment.APP_DETAILS, "test");
    }

    public void setRealRoot(TestRealRoot realRoot) {
        this.realRoot = realRoot;
    }

    @Override
    public final void connect() {
        // do nothing
    }

    @Override
    public final void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(realRoot != null)
                realRoot.distributeMessage(message);
        } catch(Throwable t) {
            getLog().e("Could not send message to real root", t);
        }
    }
}
