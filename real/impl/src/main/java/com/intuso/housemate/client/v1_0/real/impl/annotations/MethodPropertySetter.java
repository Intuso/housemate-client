package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Property implementation for annotated properties
 */
public class MethodPropertySetter<O> implements Property.Listener<RealPropertyImpl<O>> {

    private final Logger logger;
    private final Method method;
    private final Object instance;

    public MethodPropertySetter(Logger logger, Method method, Object instance) {
        this.logger = logger;
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void valueChanging(RealPropertyImpl<O> value) {
        // do nothing
    }

    @Override
    public void valueChanged(RealPropertyImpl<O> property) {
        try {
            method.invoke(instance, property.getValue());
        } catch(IllegalAccessException|InvocationTargetException e) {
            logger.error("Failed to update property method {}", method.getName(), e);
        }
    }
}