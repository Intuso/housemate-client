package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.housemate.object.v1_0.api.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <O> the type of the parameter's value
 */
public class RealParameter<O>
        extends RealObject<ParameterData, NoChildrenData, RealObject<NoChildrenData, ? ,?, ?>, Parameter.Listener<? super RealParameter<O>>>
        implements Parameter<RealParameter<O>> {

    private RealType<?, ?, O> type;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @param type the type of the parameter's value
     */
    public RealParameter(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealType<?, ?, O> type) {
        super(log, listenersFactory, new ParameterData(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public final RealType<?, ?, O> getType() {
        return type;
    }
}
