package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
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

    private final TYPE type;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxySubType(Logger logger, ListenersFactory listenersFactory, ProxyObject.Factory<TYPE> typeFactory) {
        super(logger, SubType.Data.class, listenersFactory);
        type = typeFactory.create(ChildUtil.logger(logger, SubType.TYPE_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        type.init(ChildUtil.name(name, SubType.TYPE_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        type.uninit();
    }

    @Override
    public TYPE getType() {
        return type;
    }
}
