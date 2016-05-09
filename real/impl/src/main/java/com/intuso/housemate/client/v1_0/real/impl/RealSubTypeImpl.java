package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.real.api.RealSubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <O> the type of the sub type's value
 */
public final class RealSubTypeImpl<O>
        extends RealObject<SubType.Data, SubType.Listener<? super RealSubTypeImpl<O>>>
        implements RealSubType<O, RealTypeImpl<O>, RealSubTypeImpl<O>> {

    private final RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param type
     */
    public RealSubTypeImpl(Logger logger,
                           SubType.Data data,
                           ListenersFactory listenersFactory,
                           RealTypeImpl<O> type) {
        super(logger, data, listenersFactory);
        this.type = type;
    }

    @Override
    public final RealTypeImpl<O> getType() {
        return type;
    }
}
