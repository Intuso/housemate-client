package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.Automation;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            CONDITION extends ProxyCondition<?, ?, ?, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<ConditionData, CONDITION, CONDITIONS>,
            TASK extends ProxyTask<?, ?, ?, TASK>,
            TASKS extends ProxyList<TaskData, TASK, TASKS>,
            AUTOMATION extends ProxyAutomation<COMMAND, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends ProxyObject<AutomationData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, AUTOMATION, Automation.Listener<? super AUTOMATION>>
        implements Automation<COMMAND, COMMAND, COMMAND, COMMAND, VALUE, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>,
            ProxyFailable<VALUE>,
            ProxyRemoveable<COMMAND>,
            ProxyRenameable<COMMAND>,
            ProxyRunnable<COMMAND, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyAutomation(Log log, ListenersFactory listenersFactory, AutomationData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRenameCommand() {
        return (COMMAND) getChild(AutomationData.RENAME_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(AutomationData.REMOVE_ID);
    }

    public final boolean isRunning() {
        VALUE running = getRunningValue();
        return running.getValue() != null && running.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(running.getValue().getFirstValue()) : false;
    }

    @Override
    public VALUE getRunningValue() {
        return (VALUE) getChild(AutomationData.RUNNING_ID);
    }

    @Override
    public COMMAND getStartCommand() {
        return (COMMAND) getChild(AutomationData.START_ID);
    }

    @Override
    public COMMAND getStopCommand() {
        return (COMMAND) getChild(AutomationData.STOP_ID);
    }

    public final String getError() {
        VALUE error = getErrorValue();
        return error.getValue() != null ? error.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return (VALUE) getChild(AutomationData.ERROR_ID);
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(AutomationData.NEW_NAME, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                String oldName = getData().getName();
                String newName = message.getPayload().getValue();
                getData().setName(newName);
                for(Automation.Listener<? super AUTOMATION> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        addChildLoadedListener(AutomationData.RUNNING_ID, new ChildLoadedListener<AUTOMATION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(AUTOMATION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getRunningValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Automation.Listener<? super AUTOMATION> listener : getObjectListeners())
                            listener.running(getThis(), isRunning());
                    }
                }));
            }
        });
        addChildLoadedListener(AutomationData.ERROR_ID, new ChildLoadedListener<AUTOMATION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(AUTOMATION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Automation.Listener<? super AUTOMATION> listener : getObjectListeners())
                            listener.error(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public CONDITIONS getConditions() {
        return (CONDITIONS) getChild(AutomationData.CONDITIONS_ID);
    }

    @Override
    public TASKS getSatisfiedTasks() {
        return (TASKS) getChild(AutomationData.SATISFIED_TASKS_ID);
    }

    @Override
    public TASKS getUnsatisfiedTasks() {
        return (TASKS) getChild(AutomationData.UNSATISFIED_TASKS_ID);
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return (COMMAND) getChild(AutomationData.ADD_CONDITION_ID);
    }

    @Override
    public COMMAND getAddSatisifedTaskCommand() {
        return (COMMAND) getChild(AutomationData.ADD_SATISFIED_TASK_ID);
    }

    @Override
    public COMMAND getAddUnsatisifedTaskCommand() {
        return (COMMAND) getChild(AutomationData.ADD_UNSATISFIED_TASK_ID);
    }
}
