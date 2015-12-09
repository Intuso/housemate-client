package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.object.v1_0.api.Condition;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            CONDITION extends ProxyCondition<COMMAND, VALUE, PROPERTY, PROPERTIES, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<ConditionData, CONDITION, CONDITIONS>>
        extends ProxyObject<ConditionData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, CONDITION, Condition.Listener<? super CONDITION>>
        implements Condition<COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, COMMAND, CONDITION, CONDITIONS, CONDITION>,
            ProxyFailable<VALUE>,
            ProxyRemoveable<COMMAND>,
            ProxyUsesDriver<PROPERTY, VALUE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyCondition(Logger logger, ListenersFactory listenersFactory, ConditionData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        final java.util.List<ListenerRegistration> result = super.registerListeners();
        addChildLoadedListener(ConditionData.SATISFIED_ID, new ChildLoadedListener<CONDITION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(CONDITION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getSatisfiedValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Condition.Listener<? super CONDITION> listener : getObjectListeners())
                            listener.conditionSatisfied(getThis(), isSatisfied());
                    }
                }));
            }
        });
        addChildLoadedListener(ConditionData.ERROR_ID, new ChildLoadedListener<CONDITION, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(CONDITION object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new Value.Listener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(Condition.Listener<? super CONDITION> listener : getObjectListeners())
                            listener.error(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return (COMMAND) getChild(ConditionData.ADD_CONDITION_ID);
    }

    @Override
    public CONDITIONS getConditions() {
        return (CONDITIONS) getChild(ConditionData.CONDITIONS_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(ConditionData.REMOVE_ID);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(ConditionData.PROPERTIES_ID);
    }

    @Override
    public final VALUE getErrorValue() {
        return (VALUE) getChild(ConditionData.ERROR_ID);
    }

    @Override
    public PROPERTY getDriverProperty() {
        return (PROPERTY) getChild(ConditionData.DRIVER_ID);
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return (VALUE) getChild(ConditionData.DRIVER_LOADED_ID);
    }

    @Override
    public final boolean isDriverLoaded() {
        VALUE driverLoaded = getDriverLoadedValue();
        return driverLoaded.getValue() != null
                && driverLoaded.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoaded.getValue().getFirstValue());
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getValue() != null ? error.getValue().getFirstValue() : null;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return (VALUE) getChild(ConditionData.SATISFIED_ID);
    }

    public final boolean isSatisfied() {
        VALUE satisfied = getSatisfiedValue();
        return satisfied.getValue() != null && satisfied.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(satisfied.getValue().getFirstValue()) : false;
    }
}
