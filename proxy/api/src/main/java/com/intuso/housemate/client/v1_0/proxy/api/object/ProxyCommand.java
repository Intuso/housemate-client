package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;
import java.util.Map;

/**
 * @param <VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
            VALUE extends ProxyValue<?, VALUE>,
            PARAMETERS extends ProxyList<? extends ProxyParameter<?, ?>>,
            COMMAND extends ProxyCommand<VALUE, PARAMETERS, COMMAND>>
        extends ProxyObject<Command.Data, Command.Listener<? super COMMAND>>
        implements Command<Type.InstanceMap, VALUE, PARAMETERS, COMMAND>, MessageListener {

    private final VALUE enabledValue;
    private final PARAMETERS parameters;

    private Session session;
    private MessageProducer performProducer;
    private MessageConsumer performStatusConsumer;

    private int nextId;
    private final Map<String, Command.PerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     */
    protected ProxyCommand(Logger logger,
                           ListenersFactory listenersFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<PARAMETERS> parametersFactory) {
        super(logger, Command.Data.class, listenersFactory);
        enabledValue = valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID), session);
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID), session);
        this.session = session;
        performProducer = session.createProducer(session.createQueue(ChildUtil.name(name, Command.PERFORM_ID)));
        performStatusConsumer = session.createConsumer(session.createTopic(ChildUtil.name(name, Command.PERFORM_STATUS_ID)));
        performStatusConsumer.setMessageListener(this);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performProducer != null) {
            try {
                performProducer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform producer");
            }
            performProducer = null;
        }
        if(performStatusConsumer != null) {
            try {
                performStatusConsumer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform status producer");
            }
            performStatusConsumer = null;
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

    public boolean isEnabled() {
        return enabledValue != null
                && enabledValue.getValue() != null
                && enabledValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(enabledValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getEnabledValue() {
        return enabledValue;
    }

    @Override
    public PARAMETERS getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final void perform(Command.PerformListener<? super COMMAND> listener) {
        perform(new Type.InstanceMap(), listener);
    }

    private COMMAND getThis() {
        return (COMMAND) this;
    }

    @Override
    public final synchronized void perform(Type.InstanceMap values, Command.PerformListener<? super COMMAND> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        try {
            StreamMessage streamMessage = session.createStreamMessage();
            streamMessage.writeObject(new Command.PerformData(id, values));
            performProducer.send(streamMessage);
        } catch(JMSException e) {
            throw new HousemateException("Failed to send perform message", e);
        }
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                Object object = streamMessage.readObject();
                if(object instanceof Command.PerformStatusData) {
                    PerformStatusData performStatusData = (PerformStatusData) object;
                    if(listenerMap.containsKey(performStatusData.getOpId())) {
                        if(performStatusData.isFinished()) {
                            if(performStatusData.getError() == null)
                                listenerMap.remove(performStatusData.getOpId()).commandFinished(getThis());
                            else
                                listenerMap.remove(performStatusData.getOpId()).commandFailed(getThis(), performStatusData.getError());
                        } else
                            listenerMap.get(performStatusData.getOpId()).commandStarted(getThis());
                    }
                    // todo call object listeners
                } else
                    logger.warn("Read message object that wasn't a {}", PerformStatusData.class.getName());
            } catch(JMSException e) {
                logger.error("Failed to read object from message", e);
            }
        } else
            logger.warn("Received message that wasn't a {}", StreamMessage.class.getName());
    }
}
