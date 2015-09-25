package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.object.v1_0.api.Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyHardware<
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            HARDWARE extends ProxyHardware<PROPERTIES, HARDWARE>>
        extends ProxyObject<HardwareData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, HARDWARE, Hardware.Listener<? super HARDWARE>>
        implements Hardware<PROPERTIES, HARDWARE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyHardware(Log log, ListenersFactory listenersFactory, HardwareData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(HardwareData.PROPERTIES_ID);
    }
}
