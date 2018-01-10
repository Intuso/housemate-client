package com.intuso.housemate.client.v1_0.real.impl.annotation;

import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class FieldPropertySetter<O> implements Property.Listener<RealPropertyImpl<O>> {

    private final Logger logger;
    private final Field field;
    private final Object instance;

    public FieldPropertySetter(Logger logger, Field field, Object instance) {
        this.logger = logger;
        this.field = field;
        this.instance = instance;
    }

    @Override
    public void valueChanging(RealPropertyImpl<O> value) {
        // do nothing
    }

    @Override
    public void valueChanged(RealPropertyImpl<O> property) {
        try {
            field.set(instance, property.getValues());
        } catch(IllegalAccessException e) {
            logger.error("Failed to update property field {}", field.getName(), e);
        }
    }
}

