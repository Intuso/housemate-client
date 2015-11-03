package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Method invoker for value updates
 */
public class ValuesInvocationHandler implements InvocationHandler {

    private final Map<Method, RealValue<Object>> values;

    public ValuesInvocationHandler(Map<Method, RealValue<Object>> values) {
        this.values = values;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        RealValue<Object> value = values.get(method);
        if(value == null)
            throw new HousemateCommsException("Could not find value instance for annotated method " + method.getName());
        value.setTypedValues(objects[0]);
        return null;
    }
}
