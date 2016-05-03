package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<TYPE extends ProxyType<?>,
            PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<Parameter.Data, Parameter.Listener<? super PARAMETER>>
        implements Parameter<TYPE, PARAMETER> {

    private final TYPE type;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyParameter(Logger logger, ListenersFactory listenersFactory, ProxyObject.Factory<TYPE> typeFactory) {
        super(logger, Parameter.Data.class, listenersFactory);
        type = typeFactory.create(ChildUtil.logger(logger, Parameter.TYPE_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        type.init(ChildUtil.name(name, Parameter.TYPE_ID), session);
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
