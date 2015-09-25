package com.intuso.housemate.client.v1_0.real.api.annotations;

import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Property implementation for annotated properties
 */
public class MethodPropertyImpl extends RealProperty<Object> {

    public MethodPropertyImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                              RealType<?, ?, Object> type, Object value, final Method method, final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        addObjectListener(new Property.Listener<RealProperty<Object>>() {

            @Override
            public void valueChanging(RealProperty<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealProperty<Object> property) {
                try {
                    method.invoke(instance, property.getTypedValue());
                } catch(IllegalAccessException|InvocationTargetException e) {
                    getLog().e("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }
}
