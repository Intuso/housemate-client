package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.object.v1_0.api.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<
            DATA extends TypeData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<DATA, CHILD_DATA, CHILD, TYPE>>
        extends ProxyObject<DATA, CHILD_DATA, CHILD, TYPE, Type.Listener<? super TYPE>>
        implements Type<TYPE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyType(Logger logger, ListenersFactory listenersFactory, DATA data) {
        super(logger, listenersFactory, data);
    }
}
