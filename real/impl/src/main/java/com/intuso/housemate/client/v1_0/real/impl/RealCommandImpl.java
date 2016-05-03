package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;
import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommandImpl
        extends RealObject<Command.Data, Command.Listener<? super RealCommandImpl>>
        implements RealCommand<RealValueImpl<Boolean>, RealListImpl<RealParameterImpl<?>>, RealCommandImpl>, MessageListener {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final RealValueImpl<Boolean> enabledValue;
    private final RealListImpl<RealParameterImpl<?>> parameters;

    private Session session;
    private MessageProducer performStatusProducer;
    private MessageConsumer performConsumer;

    /**
     * @param logger {@inheritDoc}
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param listenersFactory
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, RealParameterImpl<?>... parameters) {
        this(logger, id, name, description, listenersFactory, Arrays.asList(parameters));
    }

    /**
     * @param logger {@inheritDoc}
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param listenersFactory
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, List<RealParameterImpl<?>> parameters) {
        super(logger, new Command.Data(id, name, description), listenersFactory);
        enabledValue = new RealValueImpl<>(logger, Command.ENABLED_ID, Command.ENABLED_ID, ENABLED_DESCRIPTION, listenersFactory, new BooleanType(listenersFactory), true);
        this.parameters = new RealListImpl<>(logger, new com.intuso.housemate.client.v1_0.api.object.List.Data(Command.PARAMETERS_ID, Command.PARAMETERS_ID, "The parameters required by the command"), listenersFactory, parameters);
    }

    @Override
    protected final void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID), session);
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID), session);
        this.session = session;
        performStatusProducer = session.createProducer(session.createTopic(ChildUtil.name(name, Command.PERFORM_STATUS_ID)));
        performConsumer = session.createConsumer(session.createQueue(ChildUtil.name(name, Command.PERFORM_ID)));
        performConsumer.setMessageListener(this);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performStatusProducer != null) {
            try {
                performStatusProducer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform status producer");
            }
            performStatusProducer = null;
        }
        if(performConsumer != null) {
            try {
                performConsumer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform producer");
            }
            performConsumer = null;
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
    public RealValueImpl<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealListImpl<RealParameterImpl<?>> getParameters() {
        return parameters;
    }

    @Override
    public void perform(Type.InstanceMap values, PerformListener<? super RealCommandImpl> listener) {
        try {
            listener.commandStarted(this);
            perform(values);
            listener.commandFinished(this);
        } catch(Throwable t) {
            logger.error("Failed to perform command", t);
            listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    public abstract void perform(Type.InstanceMap values);

    @Override
    public void onMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                Object object = streamMessage.readObject();
                if (object instanceof Command.PerformData) {
                    final PerformData performData = (PerformData) object;
                    perform(performData.getInstanceMap(), new PerformListener<RealCommandImpl>() {

                        @Override
                        public void commandStarted(RealCommandImpl command) {
                            performStatus(performData.getOpId(), false, null);
                        }

                        @Override
                        public void commandFinished(RealCommandImpl command) {
                            performStatus(performData.getOpId(), true, null);
                        }

                        @Override
                        public void commandFailed(RealCommandImpl command, String error) {
                            performStatus(performData.getOpId(), true, error);
                        }
                    });
                } else
                    logger.warn("Read message object that wasn't a {}", PerformData.class.getName());
            } catch(JMSException e) {
                logger.error("Failed to read object from message", e);
            }
        } else
            logger.warn("Received message that wasn't a {}", StreamMessage.class.getName());
    }

    private void performStatus(String opId, boolean finished, String error) {
        try {
            StreamMessage streamMessage = session.createStreamMessage();
            streamMessage.writeObject(new Command.PerformStatusData(opId, finished, error));
            performStatusProducer.send(streamMessage);
        } catch(JMSException e) {
            logger.error("Failed to send perform status update ({}, {}, {})", opId, finished, error, e);
        }
    }
}
