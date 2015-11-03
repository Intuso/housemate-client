package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.ValueBase;

import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public interface RealValueBase<O,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends RealValueBase<O, LISTENER, VALUE>>
        extends ValueBase<TypeInstances, LISTENER, VALUE> {

    RealType<O> getType();

    /**
     * Gets the object representation of this value
     * @return
     */
    O getTypedValue();

    /**
     * Gets the object representation of this value
     * @return
     */
    List<O> getTypedValues();

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
     */
    void setTypedValues(O ... typedValues);

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
     */
    void setTypedValues(List<O> typedValues);
}
