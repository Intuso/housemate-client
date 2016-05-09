package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.Failable;
import com.intuso.housemate.client.v1_0.api.Removeable;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.UsesDriver;
import com.intuso.housemate.client.v1_0.api.object.Task;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyFailable;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyUsesDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public abstract class ProxyTask<
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
            TASK extends ProxyTask<COMMAND, VALUE, PROPERTY, PROPERTIES, TASK>>
        extends ProxyObject<Task.Data, Task.Listener<? super TASK>>
        implements Task<COMMAND, COMMAND, VALUE, VALUE, PROPERTY, VALUE, PROPERTIES, TASK>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final PROPERTY driverProperty;
    private final VALUE driverLoadedValue;
    private final PROPERTIES properties;
    private final VALUE executingValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyTask(Logger logger,
                          ListenersFactory listenersFactory,
                          ProxyObject.Factory<COMMAND> commandFactory,
                          ProxyObject.Factory<VALUE> valueFactory,
                          ProxyObject.Factory<PROPERTY> propertyFactory,
                          ProxyObject.Factory<PROPERTIES> propertiesFactory) {
        super(logger, Task.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Task.PROPERTIES_ID));
        executingValue = valueFactory.create(ChildUtil.logger(logger, Task.EXECUTING_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), session);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), session);
        properties.init(ChildUtil.name(name, Task.PROPERTIES_ID), session);
        executingValue.init(ChildUtil.name(name, Task.EXECUTING_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        properties.uninit();
        executingValue.uninit();
    }

    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null
                && driverLoadedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    public final boolean isExecuting() {
        VALUE executing = getExecutingValue();
        return executing.getValue() != null && executing.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(executing.getValue().getFirstValue()) : false;
    }

    @Override
    public final VALUE getExecutingValue() {
        return executingValue;
    }
}
