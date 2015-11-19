package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class FieldProperty extends RealPropertyImpl<Object> {

    @Inject
    public FieldProperty(Log log,
                         ListenersFactory listenersFactory,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         @Assisted RealTypeImpl<?, ?, Object> type,
                         @Nullable @Assisted("value") Object value,
                         @Assisted final Field field,
                         @Assisted("instance") final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        field.setAccessible(true);
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

    public interface Factory {
        FieldProperty create(@Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             RealTypeImpl<?, ?, Object> type,
                             @Nullable @Assisted("value") Object value,
                             Field field,
                             @Assisted("instance") Object instance);
    }
}

