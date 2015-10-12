package com.intuso.housemate.client.v1_0.real.api.factory.hardware;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.real.api.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class HardwareFactoryType extends FactoryType<HardwareDriver.Factory<?>> {

    public final static String TYPE_ID = "hardware-factory";
    public final static String TYPE_NAME = "Hardware Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new hardware";

    @Inject
    protected HardwareFactoryType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
