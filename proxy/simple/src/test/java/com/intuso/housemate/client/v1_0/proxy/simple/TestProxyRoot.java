package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.Proxy;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.junit.Ignore;
import org.slf4j.Logger;

/**
 */
@Ignore
public class TestProxyRoot extends ProxyRoot<
        SimpleProxyServer, SimpleProxyList<ServerData, SimpleProxyServer>,
        TestProxyRoot> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public TestProxyRoot(Logger logger, ListenersFactory listenersFactory, PropertyRepository properties, ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory, @Proxy Router<?> router) {
        super(logger, listenersFactory, properties, router);
        try {
            distributeMessage(new Message<Message.Payload>(new String[] {""}, RootData.APPLICATION_STATUS_TYPE, new ApplicationData.StatusPayload(Application.Status.AllowInstances)));
            distributeMessage(new Message<Message.Payload>(new String[] {""}, RootData.APPLICATION_INSTANCE_STATUS_TYPE, new ApplicationInstanceData.StatusPayload(ApplicationInstance.Status.Allowed)));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        this.objectFactory = objectFactory;
        super.addChild(objectFactory.create(logger, new ListData(ServerData.TYPES_ID, ServerData.TYPES_ID, ServerData.TYPES_ID)));
        super.addChild(objectFactory.create(logger, new ListData(ServerData.DEVICES_ID, ServerData.DEVICES_ID, ServerData.DEVICES_ID)));
        init(null);
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(HousemateData<?> data) {
        return objectFactory.create(getLogger(), data);
    }

    public void addChild(ProxyObject<?, ?, ?, ?, ?> child) {
        removeChild(child.getId());
        super.addChild(child);
        child.init(this);
    }
}
