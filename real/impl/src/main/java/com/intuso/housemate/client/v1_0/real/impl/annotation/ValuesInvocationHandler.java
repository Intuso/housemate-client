package com.intuso.housemate.client.v1_0.real.impl.annotation;

import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Method invoker for value updates
 */
public class ValuesInvocationHandler implements InvocationHandler {

    private final Map<Method, RealValueImpl<?>> values;

    public ValuesInvocationHandler(Map<Method, RealValueImpl<?>> values) {
        this.values = values;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        RealValueImpl value = values.get(method);
        if(value == null)
            throw new HousemateException("Could not find value instance for annotated method " + method.getName());
        value.setValue(objects[0]);
        return null;
    }
}
