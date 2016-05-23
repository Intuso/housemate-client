package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<TYPE extends ProxyType<?>,
            SUB_TYPE extends ProxySubType<TYPE, SUB_TYPE>>
        extends ProxyObject<SubType.Data, SubType.Listener<? super SUB_TYPE>>
        implements SubType<TYPE, SUB_TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxySubType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, SubType.Data.class, listenersFactory);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }
}
