package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.collect.Maps;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

/**
 * @param <ENABLED_VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETER> the type of the parameters
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
            ENABLED_VALUE extends ProxyValue<?, ENABLED_VALUE>,
            PARAMETER extends ProxyParameter<PARAMETER>,
            PARAMETERS extends ProxyList<ParameterData, PARAMETER, PARAMETERS>,
            COMMAND extends ProxyCommand<ENABLED_VALUE, PARAMETER, PARAMETERS, COMMAND>>
        extends ProxyObject<CommandData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, COMMAND, Command.Listener<? super COMMAND>>
        implements Command<TypeInstanceMap, ENABLED_VALUE, PARAMETERS, COMMAND> {

    private int nextId;
    private Map<String, Command.PerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected ProxyCommand(Log log, ListenersFactory listenersFactory, CommandData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CommandData.PERFORMING_TYPE, new Message.Receiver<CommandData.PerformingPayload>() {
            @Override
            public void messageReceived(Message<CommandData.PerformingPayload> message) {
                Command.PerformListener<? super COMMAND> performer = listenerMap.get(message.getPayload().getOpId());
                if(message.getPayload().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(getThis());
                    for(Command.Listener<? super COMMAND> listener : getObjectListeners())
                        listener.commandStarted(getThis(), "");
                } else {
                    listenerMap.remove(message.getPayload().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(Command.Listener<? super COMMAND> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(CommandData.FAILED_TYPE, new Message.Receiver<CommandData.FailedPayload>() {
            @Override
            public void messageReceived(Message<CommandData.FailedPayload> message) {
                Command.PerformListener<? super COMMAND> performer = listenerMap.remove(message.getPayload().getOpId());
                if(performer != null)
                    performer.commandFailed(getThis(), message.getPayload().getCause());
                for(Command.Listener<? super COMMAND> listener : getObjectListeners())
                    listener.commandFailed(getThis(), message.getPayload().getCause());
            }
        }));
        addChildLoadedListener(CommandData.ENABLED_ID, new ChildLoadedListener<COMMAND, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(COMMAND object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getEnabledValue().addObjectListener(new Value.Listener<ENABLED_VALUE>() {
                    @Override
                    public void valueChanging(ENABLED_VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(ENABLED_VALUE value) {
                        // call our own listeners
                        boolean enabled = isEnabled();
                        for(Command.Listener<? super COMMAND> listener : getObjectListeners())
                            listener.commandEnabled(getThis(), enabled);
                    }
                }));
            }
        });
        return result;
    }

    public boolean isEnabled() {
        ENABLED_VALUE enabled = getEnabledValue();
        return enabled != null
                && enabled.getValue() != null
                && enabled.getValue().getFirstValue() != null
                && Boolean.parseBoolean(enabled.getValue().getFirstValue());
    }

    @Override
    public ENABLED_VALUE getEnabledValue() {
        return (ENABLED_VALUE) getChild(CommandData.ENABLED_ID);
    }

    @Override
    public PARAMETERS getParameters() {
        return (PARAMETERS) getChild(CommandData.PARAMETERS_ID);
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final synchronized void perform(Command.PerformListener<? super COMMAND> listener) {
        perform(new TypeInstanceMap(), listener);
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, Command.PerformListener<? super COMMAND> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        sendMessage(CommandData.PERFORM_TYPE, new CommandData.PerformPayload(id, values));
    }
}
