package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueBaseData;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.ValueBase;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<?, ?, ?, ?>,
        LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ProxyValueBase<DATA, CHILD_DATA, CHILD, TYPE, LISTENER, VALUE>>
        extends ProxyObject<DATA, CHILD_DATA, CHILD, VALUE, LISTENER>
        implements ValueBase<TypeInstances, LISTENER, VALUE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValueBase(Logger logger, ListenersFactory listenersFactory, DATA data) {
        super(listenersFactory, logger, data);
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ValueBaseData.VALUE_ID, new Message.Receiver<TypeData.TypeInstancesPayload>() {
            @Override
            public void messageReceived(Message<TypeData.TypeInstancesPayload> stringMessageValueMessage) {
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getData().setTypeInstances(stringMessageValueMessage.getPayload().getTypeInstances());
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public final TypeInstances getValue() {
        return getData().getTypeInstances();
    }
}
