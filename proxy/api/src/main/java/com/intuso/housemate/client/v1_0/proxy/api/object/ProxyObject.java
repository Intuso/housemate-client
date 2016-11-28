package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Serialiser;
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

    public final static String PROXY = "proxy";

    protected final Logger logger;
    protected final Listeners<LISTENER> listeners;
    private final Class<DATA> dataClass;

    protected DATA data;
    private Session session;
    private MessageConsumer consumer;

    /**
     * @param logger the log
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ListenersFactory listenersFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.dataClass = dataClass;
        this.listeners = listenersFactory.create();
    }

    @Override
    public String getId() {
        return data == null ? null : data.getId();
    }

    @Override
    public String getName() {
        return data == null ? null : data.getName();
    }

    @Override
    public String getDescription() {
        return data == null ? null : data.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(LISTENER listener) {
        return listeners.addListener(listener);
    }

    protected final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init");
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(session.createTopic(name + "?consumer.retroactive=true"));
        Message retained = consumer.receiveNoWait();
        if(retained != null) {
            processReceivedMessage(retained);
        }
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                processReceivedMessage(message);
            }
        });
        initChildren(name, connection);
    }

    private void processReceivedMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                java.lang.Object messageObject = streamMessage.readObject();
                if(messageObject instanceof byte[]) {
                    java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                    if (dataClass.isAssignableFrom(object.getClass())) {
                        data = (DATA) object;
                        dataUpdated();
                    } else
                        logger.warn("Deserialised message object that wasn't a {}", dataClass.getName());
                } else
                    logger.warn("Message data was not a {}", byte[].class.getName());
            } catch(JMSException e) {
                logger.error("Could not read object from received message", e);
            }
        } else
            logger.error("Received message that wasn't a {}", StreamMessage.class.getName());
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

    protected final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(consumer != null) {
            try {
                consumer.close();
            } catch (JMSException e) {
                logger.error("Failed to close consumer");
            }
            consumer = null;
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

    protected void uninitChildren() {}

    protected void dataUpdated() {}

    public interface Factory<OBJECT extends ProxyObject> {
        OBJECT create(Logger logger);
    }
}
