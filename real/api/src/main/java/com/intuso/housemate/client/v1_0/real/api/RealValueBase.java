package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.ValueBase;

import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public interface RealValueBase<DATA extends ValueBase.Data,
        O,
        TYPE extends RealType<O, ?>,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends RealValueBase<DATA, O, TYPE, LISTENER, VALUE>>
        extends ValueBase<DATA,
        O,
        TYPE,
        LISTENER,
        VALUE> {

    /**
     * Gets the object representation of this value
     * @return
     */
    Iterable<O> getValues();

    void setValue(O value);

    /**
     * Sets the object representation of this value
     * @param values the new values
     */
    void setValues(List<O> values);
}
