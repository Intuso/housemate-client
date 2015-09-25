package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<
            TYPE extends ProxyType<?, ?, ?, ?>,
            SET_COMMAND extends ProxyCommand<?, ?, ?, SET_COMMAND>,
            PROPERTY extends ProxyProperty<TYPE, SET_COMMAND, PROPERTY>>
        extends ProxyValueBase<PropertyData, CommandData, SET_COMMAND, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>
        implements Property<TypeInstances, SET_COMMAND, PROPERTY> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyProperty(Log log, ListenersFactory listenersFactory, PropertyData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public void set(final TypeInstances value, Command.PerformListener<? super SET_COMMAND> listener) {
        TypeInstanceMap values = new TypeInstanceMap();
        values.getChildren().put(PropertyData.VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }

    @Override
    public SET_COMMAND getSetCommand() {
        return getChild(PropertyData.SET_COMMAND_ID);
    }
}
