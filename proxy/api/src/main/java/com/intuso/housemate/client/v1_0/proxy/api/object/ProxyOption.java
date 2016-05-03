package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            SUB_TYPES extends ProxyList<? extends ProxySubType<?, ?>>,
            OPTION extends ProxyOption<SUB_TYPES, OPTION>>
        extends ProxyObject<Option.Data, Option.Listener<? super OPTION>>
        implements Option<SUB_TYPES, OPTION> {

    private final SUB_TYPES subTypes;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyOption(Logger logger,
                       ListenersFactory listenersFactory,
                       ProxyObject.Factory<SUB_TYPES> subTypesFactory) {
        super(logger, Option.Data.class, listenersFactory);
        subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        subTypes.init(ChildUtil.name(name, Option.SUB_TYPES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return subTypes;
    }
}
