package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyAutomation;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.AutomationData;
import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TaskData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:15
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyAutomation extends ProxyAutomation<
        SimpleProxyCommand,
        SimpleProxyValue,
        SimpleProxyCondition,
        SimpleProxyList<ConditionData, SimpleProxyCondition>, SimpleProxyTask, SimpleProxyList<TaskData, SimpleProxyTask>, SimpleProxyAutomation> {

    private final ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory;

    @Inject
    public SimpleProxyAutomation(Logger logger,
                                 ListenersFactory listenersFactory,
                                 ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> objectFactory,
                                 @Assisted AutomationData data) {
        super(logger, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return objectFactory.create(data);
    }
}
