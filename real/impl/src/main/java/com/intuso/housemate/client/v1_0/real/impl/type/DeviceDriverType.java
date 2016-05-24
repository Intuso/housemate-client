package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Types;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class DeviceDriverType extends FactoryType<DeviceDriver.Factory<?>> {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new device";

    @Inject
    protected DeviceDriverType(@Types Logger logger, ListenersFactory listenersFactory, RealOptionImpl.Factory optionFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, optionFactory);
    }
}
