package com.intuso.housemate.client.v1_0.api.object;

import com.intuso.housemate.client.v1_0.api.object.view.ValueView;

/**
 * @param <DATA_TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface Value<DATA_TYPE,
            TYPE extends Type<?>,
            VALUE extends Value<?, ?, ?>>
        extends ValueBase<Value.Data, DATA_TYPE, TYPE, Value.Listener<? super VALUE>, ValueView, VALUE> {

    /**
     *
     * Listener interface for values
     */
    interface Listener<VALUE extends Value<?, ?, ?>> extends ValueBase.Listener<VALUE> {}

    /**
     *
     * Interface to show that the implementing object has a list of values
     */
    interface Container<VALUES extends Iterable<? extends Value<?, ?, ?>>> {

        /**
         * Gets the value list
         * @return the value list
         */
        VALUES getValues();
    }

    /**
     *
     * Data object for a value
     */
    final class Data extends ValueBase.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "value";

        public Data() {}

        public Data(String id, String name, String description, String type, int minValues, int maxValues, Type.Instances values) {
            super(OBJECT_CLASS, id, name,  description, type, minValues, maxValues, values);
        }
    }
}
