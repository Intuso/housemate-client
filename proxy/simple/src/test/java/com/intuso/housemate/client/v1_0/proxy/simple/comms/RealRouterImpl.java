package com.intuso.housemate.client.v1_0.proxy.simple.comms;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.simple.TestProxyRoot;
import com.intuso.housemate.comms.v1_0.api.BaseRouter;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class RealRouterImpl extends BaseRouter<RealRouterImpl> {

    private TestProxyRoot proxyRoot;

    @Inject
    public RealRouterImpl(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory);
        connectionEstablished();
    }

    public void setProxyRoot(TestProxyRoot proxyRoot) {
        this.proxyRoot = proxyRoot;
    }

    @Override
    public final void connect() {
        // do nothing
        connectionEstablished();
    }

    @Override
    public final void disconnect() {
        // do nothing
        connectionLost(false);
    }

    @Override
    protected void sendMessageNow(Message<?> message) {
        if(proxyRoot != null)
            proxyRoot.distributeMessage((Message<Message.Payload>) message);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            sendMessageNow(message);
        } catch(Throwable t) {
            getLog().e("Could not send message to proxy root", t);
        }
    }
}
