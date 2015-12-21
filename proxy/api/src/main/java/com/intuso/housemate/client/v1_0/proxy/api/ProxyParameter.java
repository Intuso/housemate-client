package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.housemate.object.v1_0.api.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<
            PARAMETER extends ProxyParameter<PARAMETER>>
        extends ProxyObject<ParameterData, NoChildrenData, NoChildrenProxyObject, PARAMETER, Parameter.Listener<? super PARAMETER>>
        implements Parameter<PARAMETER> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyParameter(Logger logger, ListenersFactory listenersFactory, ParameterData data) {
        super(listenersFactory, logger, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }
}
