package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.comms.v1_0.api.payload.TaskData;
import com.intuso.housemate.object.v1_0.api.Task;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public abstract class ProxyTask<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            TASK extends ProxyTask<COMMAND, VALUE, PROPERTIES, TASK>>
        extends ProxyObject<TaskData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, TASK, Task.Listener<? super TASK>>
        implements Task<COMMAND, VALUE, VALUE, PROPERTIES, TASK>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyTask(Log log, ListenersFactory listenersFactory, TaskData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        final java.util.List<ListenerRegistration>result = super.registerListeners();
        addChildLoadedListener(TaskData.EXECUTING_ID, new ChildLoadedListener<TASK, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(TASK object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getExecutingValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Task.Listener listener : getObjectListeners())
                            listener.taskExecuting(getThis(), isExecuting());
                    }
                }));
            }
        });
        addChildLoadedListener(TaskData.ERROR_ID, new ChildLoadedListener<TASK, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(TASK object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Task.Listener listener : getObjectListeners())
                            listener.taskError(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(TaskData.REMOVE_ID);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(TaskData.PROPERTIES_ID);
    }

    @Override
    public final VALUE getErrorValue() {
        return (VALUE) getChild(TaskData.ERROR_ID);
    }

    public final String getError() {
        VALUE error = getErrorValue();
        return error.getValue() != null ? error.getValue().getFirstValue() : null;
    }

    @Override
    public final VALUE getExecutingValue() {
        return (VALUE) getChild(TaskData.EXECUTING_ID);
    }

    public final boolean isExecuting() {
        VALUE executing = getExecutingValue();
        return executing.getValue() != null && executing.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(executing.getValue().getFirstValue()) : false;
    }
}
