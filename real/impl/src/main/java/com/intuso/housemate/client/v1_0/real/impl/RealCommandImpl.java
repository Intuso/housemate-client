package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommandImpl
        extends RealObject<CommandData, HousemateData<?>, RealObject<?, ?, ?, ?>, Command.Listener<? super RealCommand>>
        implements RealCommand {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final RealValueImpl<Boolean> enabledValue;
    private final RealList<RealParameter<?>> parameters;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealParameter<?>... parameters) {
        this(logger, listenersFactory, id, name, description, Arrays.asList(parameters));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, List<RealParameter<?>> parameters) {
        super(logger, listenersFactory, new CommandData(id, name, description));
        enabledValue = new RealValueImpl<>(logger, listenersFactory, CommandData.ENABLED_ID, CommandData.ENABLED_ID, ENABLED_DESCRIPTION, new BooleanType(logger, listenersFactory), true);
        this.parameters = (RealList)new RealListImpl<ParameterData, RealParameterImpl<?>>(logger, listenersFactory, CommandData.PARAMETERS_ID, CommandData.PARAMETERS_ID, "The parameters required by the command");
        for(RealParameter<?> parameter : parameters)
            this.parameters.add(parameter);
        addChild(enabledValue);
        addChild((RealListImpl)this.parameters);
    }

    @Override
    protected final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CommandData.PERFORM_TYPE, new Message.Receiver<CommandData.PerformPayload>() {
            @Override
            public void messageReceived(final Message<CommandData.PerformPayload> message) {
                perform(message.getPayload().getValues(), new PerformListener<RealCommand>() {

                    @Override
                    public void commandStarted(RealCommand command) {
                        sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOpId(), true));
                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOpId(), false));
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {
                        sendMessage(CommandData.FAILED_TYPE, new CommandData.FailedPayload(message.getPayload().getOpId(), error));
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public RealValue<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealList<RealParameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public void perform(TypeInstanceMap values, PerformListener<? super RealCommand> listener) {
        try {
            listener.commandStarted(this);
            perform(values);
            listener.commandFinished(this);
        } catch(Throwable t) {
            getLogger().error("Failed to perform command", t);
            listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    public abstract void perform(TypeInstanceMap values);
}
