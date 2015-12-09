package com.intuso.housemate.client.v1_0.proxy.simple.comms;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.simple.TestRealRoot;
import com.intuso.housemate.comms.v1_0.api.BaseRouter;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class ProxyRouterImpl extends BaseRouter<ProxyRouterImpl> {

    private TestRealRoot realRoot;

    @Inject
    public ProxyRouterImpl(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory);
        connectionEstablished();
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
    protected void sendMessageNow(Message<?> message) {
        if(realRoot != null)
            realRoot.distributeMessage((Message<Message.Payload>) message);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            sendMessageNow(message);
        } catch(Throwable t) {
            getLogger().error("Could not send message to real root", t);
        }
    }
}
