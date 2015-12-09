package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyServer;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyServer extends ProxyServer<
        SimpleProxyApplication, SimpleProxyList<ApplicationData, SimpleProxyApplication>,
        SimpleProxyAutomation, SimpleProxyList<AutomationData, SimpleProxyAutomation>,
        SimpleProxyDevice, SimpleProxyList<DeviceData, SimpleProxyDevice>,
        SimpleProxyHardware, SimpleProxyList<HardwareData, SimpleProxyHardware>,
        SimpleProxyType, SimpleProxyList<TypeData<?>, SimpleProxyType>,
        SimpleProxyUser, SimpleProxyList<UserData, SimpleProxyUser>,
        SimpleProxyCommand, SimpleProxyServer> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyServer(Logger logger,
                             ListenersFactory listenersFactory,
                             ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             @Assisted ServerData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
