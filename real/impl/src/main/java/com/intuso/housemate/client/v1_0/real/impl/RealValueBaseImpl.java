package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Serialiser;
import com.intuso.housemate.client.v1_0.api.object.ValueBase;
import com.intuso.housemate.client.v1_0.real.api.RealValueBase;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;
import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <DATA> the type of the data object
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBaseImpl<O,
            DATA extends ValueBase.Data,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends RealValueBase<O, RealTypeImpl<O>, LISTENER, VALUE>>
        extends RealObject<DATA, LISTENER>
        implements RealValueBase<O, RealTypeImpl<O>, LISTENER, VALUE> {

    private final RealTypeImpl<O> type;

    private Session session;
    private MessageProducer valueProducer;

    private Iterable<O> values;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBaseImpl(Logger logger, DATA data, ListenersFactory listenersFactory, RealTypeImpl<O> type, Iterable<O> values) {
        super(logger, data, listenersFactory);
        this.type = type;
        this.values = values;
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        valueProducer = session.createProducer(session.createTopic(ChildUtil.name(name, ValueBase.VALUE_ID)));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueProducer != null) {
            try {
                valueProducer.close();
            } catch(JMSException e) {
                logger.error("Failed to close value producer");
            }
            valueProducer = null;
        }
        if(session != null) {
            try {
                session.close();
            } catch(JMSException e) {
                logger.error("Failed to close session");
            }
            session = null;
        }
    }

    @Override
    public RealTypeImpl<O> getType() {
        return type;
    }

    @Override
    public O getValue() {
        return values != null && values.iterator().hasNext() ? values.iterator().next() : null;
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public Iterable<O> getValues() {
        return values;
    }

    @Override
    public void setValue(O value) {
        setValues(Lists.newArrayList(value));
    }

    /**
     * Sets the object representation of this value
     * @param values the new value
     */
    public final void setValues(List<O> values) {
        this.values = values;
        try {
            StreamMessage streamMessage = session.createStreamMessage();
            streamMessage.writeObject(Serialiser.serialise(RealTypeImpl.serialiseAll(type, values)));
            valueProducer.send(streamMessage);
        } catch(JMSException e) {
            logger.error("Failed to send value update", e);
        }
    }
}
