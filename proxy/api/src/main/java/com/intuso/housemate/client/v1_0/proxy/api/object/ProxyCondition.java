package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.Failable;
import com.intuso.housemate.client.v1_0.api.Removeable;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.UsesDriver;
import com.intuso.housemate.client.v1_0.api.object.Condition;
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
 * @param <COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>>,
            CONDITION extends ProxyCondition<COMMAND, VALUE, PROPERTY, PROPERTIES, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<CONDITION>>
        extends ProxyObject<Condition.Data, Condition.Listener<? super CONDITION>>
        implements Condition<COMMAND, COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, COMMAND, CONDITIONS, CONDITION>,
        ProxyRemoveable<COMMAND>,
        ProxyFailable<VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final PROPERTY driverProperty;
    private final VALUE driverLoadedValue;
    private final PROPERTIES properties;
    private final CONDITIONS conditions;
    private final COMMAND addConditionCommand;
    private final VALUE satisfiedValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyCondition(Logger logger,
                          ListenersFactory listenersFactory,
                          ProxyObject.Factory<COMMAND> commandFactory,
                          ProxyObject.Factory<VALUE> valueFactory,
                          ProxyObject.Factory<PROPERTY> propertyFactory,
                          ProxyObject.Factory<PROPERTIES> propertiesFactory,
                          ProxyObject.Factory<CONDITIONS> conditionsFactory) {
        super(logger, Condition.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Condition.PROPERTIES_ID));
        conditions = conditionsFactory.create(ChildUtil.logger(logger, Condition.CONDITIONS_ID));
        addConditionCommand = commandFactory.create(ChildUtil.logger(logger, Condition.ADD_CONDITION_ID));
        satisfiedValue = valueFactory.create(ChildUtil.logger(logger, Condition.SATISFIED_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), session);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), session);
        properties.init(ChildUtil.name(name, Condition.PROPERTIES_ID), session);
        conditions.init(ChildUtil.name(name, Condition.CONDITIONS_ID), session);
        addConditionCommand.init(ChildUtil.name(name, Condition.ADD_CONDITION_ID), session);
        satisfiedValue.init(ChildUtil.name(name, Condition.SATISFIED_ID), session);
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
        conditions.uninit();
        addConditionCommand.uninit();
        satisfiedValue.uninit();
    }

    @Override
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

    @Override
    public COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    public final boolean isSatisfied() {
        return satisfiedValue.getValue() != null && satisfiedValue.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(satisfiedValue.getValue().getFirstValue()) : false;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return satisfiedValue;
    }
}
