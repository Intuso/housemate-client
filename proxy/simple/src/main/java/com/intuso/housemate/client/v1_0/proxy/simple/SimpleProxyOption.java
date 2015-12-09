package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyOption;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyOption extends ProxyOption<
        SimpleProxySubType,
        SimpleProxyList<SubTypeData, SimpleProxySubType>,
        SimpleProxyOption> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyOption(Logger logger,
                             ListenersFactory listenersFactory,
                             ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             @Assisted OptionData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected SimpleProxyList<SubTypeData, SimpleProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return (SimpleProxyList<SubTypeData, SimpleProxySubType>) objectFactory.create(data);
    }
}
