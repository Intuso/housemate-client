package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Hardware;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyServer;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 25/01/17.
 */
public class HardwareType extends RealObjectType<SimpleProxyHardware> {

    public final static String TYPE_ID = "hardware";
    public final static String TYPE_NAME = "Hardware";
    public final static String TYPE_DESCRIPTION = "Hardware objects";

    /**
     * @param logger
     * @param listenersFactory
     * @param server           the root to get the object from
     */
    @Inject
    public HardwareType(@Type Logger logger, ListenersFactory listenersFactory, SimpleProxyServer server) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Sets.newHashSet(Hardware.Data.OBJECT_CLASS), listenersFactory, server);
    }
}
