package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyAutomation;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
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
        SimpleProxyList<SimpleProxyCondition>,
        SimpleProxyList<SimpleProxyTask>,
        SimpleProxyAutomation> {

    @Inject
    public SimpleProxyAutomation(@Assisted Logger logger,
                                 ListenersFactory listenersFactory,
                                 ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                                 ProxyObject.Factory<SimpleProxyValue> valueFactory,
                                 ProxyObject.Factory<SimpleProxyList<SimpleProxyCondition>> conditionsFactory,
                                 ProxyObject.Factory<SimpleProxyList<SimpleProxyTask>> tasksFactory) {
        super(logger, listenersFactory, commandFactory, valueFactory, conditionsFactory, tasksFactory);
    }
}
