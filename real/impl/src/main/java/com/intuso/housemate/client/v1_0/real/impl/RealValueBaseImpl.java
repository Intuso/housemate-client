package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.client.v1_0.real.api.RealValueBase;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueBaseData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.ValueBase;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBaseImpl<
        DATA extends ValueBaseData<CHILD_DATA>,
        CHILD_DATA extends HousemateData<?>,
        CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>,
        O,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends RealValueBase<O, LISTENER, VALUE>>
        extends RealObject<DATA, CHILD_DATA, CHILD, LISTENER>
        implements RealValueBase<O, LISTENER, VALUE> {

    private RealType<O> type;
    private List<O> typedValues;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBaseImpl(Logger logger, ListenersFactory listenersFactory, DATA data, RealType<O> type) {
        super(listenersFactory, logger, data);
        this.type = type;
        this.typedValues = RealTypeImpl.deserialiseAll(type, data.getTypeInstances());
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public RealType<O> getType() {
        return type;
    }

    @Override
    public TypeInstances getValue() {
        return getData().getTypeInstances();
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public O getTypedValue() {
        return typedValues != null && typedValues.size() != 0 ? typedValues.get(0) : null;
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public List<O> getTypedValues() {
        return typedValues;
    }

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
     */
    public final void setTypedValues(O ... typedValues) {
        if(typedValues == null)
            setTypedValues((List)null);
        else
            setTypedValues(Arrays.asList(typedValues));
    }

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
     */
    public final void setTypedValues(List<O> typedValues) {
        if((this.typedValues == null && typedValues == null)
                || (this.typedValues != null && typedValues != null && this.typedValues.equals(typedValues)))
            return;
        for(Listener<? super VALUE> listener : getObjectListeners())
            listener.valueChanging((VALUE)this);
        this.typedValues = typedValues;
        this.getData().setTypeInstances(RealTypeImpl.serialiseAll(getType(), typedValues));
        for(Listener<? super VALUE> listener : getObjectListeners())
            listener.valueChanged((VALUE)this);
        sendMessage(ValueData.VALUE_ID, new TypeData.TypeInstancesPayload(getValue()));
    }
}
