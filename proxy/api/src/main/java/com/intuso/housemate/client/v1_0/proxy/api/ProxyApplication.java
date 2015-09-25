package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ApplicationData;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <COMMAND> the type of the command
 * @param <APPLICATION> the type of the user
 */
public abstract class ProxyApplication<
            VALUE extends ProxyValue<?, VALUE>,
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            APPLICATION_INSTANCE extends ProxyApplicationInstance<?, ?, APPLICATION_INSTANCE>,
            APPLICATION_INSTANCES extends ProxyList<ApplicationInstanceData, APPLICATION_INSTANCE, APPLICATION_INSTANCES>,
            APPLICATION extends ProxyApplication<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, APPLICATION>>
        extends ProxyObject<ApplicationData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, APPLICATION, Application.Listener<? super APPLICATION>>
        implements Application<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, APPLICATION> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    public ProxyApplication(Log log, ListenersFactory listenersFactory, ApplicationData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public APPLICATION_INSTANCES getApplicationInstances() {
        return (APPLICATION_INSTANCES) getChild(ApplicationData.APPLICATION_INSTANCES_ID);
    }

    @Override
    public COMMAND getAllowCommand() {
        return (COMMAND) getChild(ApplicationData.ALLOW_COMMAND_ID);
    }

    @Override
    public COMMAND getSomeCommand() {
        return (COMMAND) getChild(ApplicationData.SOME_COMMAND_ID);
    }

    @Override
    public COMMAND getRejectCommand() {
        return (COMMAND) getChild(ApplicationData.REJECT_COMMAND_ID);
    }

    public Application.Status getStatus() {
        String value = getStatusValue().getValue().getFirstValue();
        if(value == null)
            return null;
        return Application.Status.valueOf(value);
    }

    @Override
    public VALUE getStatusValue() {
        return (VALUE) getChild(ApplicationData.STATUS_VALUE_ID);
    }
}
