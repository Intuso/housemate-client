package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <VALUE> the type of the value
 * @param <COMMAND> the type of the command
 * @param <APPLICATION_INSTANCE> the type of the application instance
 */
public abstract class ProxyApplicationInstance<
            VALUE extends ProxyValue<?, ?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?>,
            APPLICATION_INSTANCE extends ProxyApplicationInstance<VALUE, COMMAND, APPLICATION_INSTANCE>>
        extends ProxyObject<ApplicationInstanceData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, APPLICATION_INSTANCE, ApplicationInstance.Listener<? super APPLICATION_INSTANCE>>
        implements ApplicationInstance<VALUE, COMMAND, APPLICATION_INSTANCE> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    public ProxyApplicationInstance(Log log, ListenersFactory listenersFactory, ApplicationInstanceData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getAllowCommand() {
        return (COMMAND) getChild(ApplicationInstanceData.ALLOW_COMMAND_ID);
    }

    @Override
    public COMMAND getRejectCommand() {
        return (COMMAND) getChild(ApplicationInstanceData.REJECT_COMMAND_ID);
    }

    public ApplicationInstance.Status getStatus() {
        String value = getStatusValue().getValue().getFirstValue();
        if(value == null)
            return null;
        return ApplicationInstance.Status.valueOf(value);
    }

    @Override
    public VALUE getStatusValue() {
        return (VALUE) getChild(ApplicationInstanceData.STATUS_VALUE_ID);
    }
}
