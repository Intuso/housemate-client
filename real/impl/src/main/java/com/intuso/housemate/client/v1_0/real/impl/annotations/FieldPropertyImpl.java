package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class FieldPropertyImpl extends RealPropertyImpl<Object> {

    public FieldPropertyImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                             RealTypeImpl<?, ?, Object> type, Object value, final Field field, final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        addObjectListener(new Property.Listener<RealProperty<Object>>() {

            @Override
            public void valueChanging(RealProperty<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealProperty<Object> property) {
                try {
                    field.set(instance, property.getTypedValue());
                } catch(IllegalAccessException e) {
                    getLog().e("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }
}
