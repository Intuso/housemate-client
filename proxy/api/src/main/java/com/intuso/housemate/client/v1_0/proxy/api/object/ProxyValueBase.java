package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;

/**
 * @param <DATA> the type of the data
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            DATA extends Object.Data,
            TYPE extends ProxyType<?>,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ProxyValueBase<DATA, TYPE, LISTENER, VALUE>>
        extends ProxyObject<DATA, LISTENER>
        implements ValueBase<Type.Instances, TYPE, LISTENER, VALUE>, MessageListener {

    private final TYPE type;
    private Session session;
    private MessageConsumer valueConsumer;

    private Type.Instances value;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValueBase(Logger logger,
                          Class<DATA> dataClass,
                          ListenersFactory listenersFactory,
                          ProxyObject.Factory<TYPE> typeFactory) {
        super(logger, dataClass, listenersFactory);
        type = typeFactory.create(ChildUtil.logger(logger, Value.TYPE_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        type.init(ChildUtil.name(name, Value.TYPE_ID), session);
        this.session = session;
        valueConsumer = session.createConsumer(session.createTopic(ChildUtil.name(name, Value.VALUE_ID)));
        valueConsumer.setMessageListener(this);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        type.uninit();
        if(valueConsumer != null) {
            try {
                valueConsumer.close();
            } catch(JMSException e) {
                logger.error("Failed to close value producer");
            }
            valueConsumer = null;
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
    public TYPE getType() {
        return type;
    }

    @Override
    public Type.Instances getValue() {
        return value;
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                java.lang.Object messageObject = streamMessage.readObject();
                if(messageObject instanceof byte[]) {
                    java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                    if(object instanceof Type.Instances) {
                        value = (Type.Instances) object;
                        // todo call object listeners
                    } else
                        logger.warn("Deserialised message object that wasn't a {}", Type.Instances.class.getName());
                } else
                    logger.warn("Message data was not a byte[]");
            } catch(JMSException e) {
                logger.error("Failed to read object from message", e);
            }
        } else
            logger.warn("Received message that wasn't a {}", StreamMessage.class.getName());
    }
}
