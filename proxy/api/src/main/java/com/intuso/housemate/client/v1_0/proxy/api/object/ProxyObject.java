package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
            DATA extends Object.Data,
            LISTENER extends Object.Listener> implements Object<LISTENER> {

    protected final Logger logger;
    protected final Listeners<LISTENER> listeners;
    private final Class<DATA> dataClass;

    protected DATA data;
    private MessageConsumer consumer;

    /**
     * @param logger the log
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ListenersFactory listenersFactory) {
        this.logger = logger;
        this.dataClass = dataClass;
        this.listeners = listenersFactory.create();
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public String getDescription() {
        return data.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(LISTENER listener) {
        return listeners.addListener(listener);
    }

    protected final void init(String name, Session session) throws JMSException {
        consumer = session.createConsumer(session.createTopic(name));
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof StreamMessage) {
                    StreamMessage streamMessage = (StreamMessage) message;
                    try {
                        java.lang.Object messageObject = streamMessage.readObject();
                        if (dataClass.isAssignableFrom(message.getClass())) {
                            data = (DATA) messageObject;
                            dataUpdated();
                        }
                    } catch(JMSException e) {
                        logger.error("Could not read object from received message", e);
                    }
                } else
                    logger.error("Received message that wasn't a {}", StreamMessage.class.getName());
            }
        });
        initChildren(name, session);
    }

    protected void initChildren(String name, Session session) throws JMSException {}

    protected final void uninit() {
        uninitChildren();
        if(consumer != null) {
            try {
                consumer.close();
            } catch (JMSException e) {
                logger.error("Failed to close consumer");
            }
            consumer = null;
        }
    }

    protected void uninitChildren() {}

    protected void dataUpdated() {}

    public interface Factory<OBJECT extends ProxyObject> {
        OBJECT create(Logger logger);
    }
}
