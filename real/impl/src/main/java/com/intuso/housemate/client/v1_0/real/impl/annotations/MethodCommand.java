package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Command implementation for annotated commands
 */
public class MethodCommand extends RealCommandImpl {

    private final Method method;
    private final Object instance;
    private final ParameterConverter parameterConverter;

    @Inject
    protected MethodCommand(ListenersFactory listenersFactory,
                            @Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted List<RealParameterImpl<?>> parameters,
                            @Assisted Method method,
                            @Assisted Object instance) {
        super(logger, id, name, description, listenersFactory, parameters);
        this.method = method;
        this.instance = instance;
        parameterConverter = new ParameterConverter(parameters);
        method.setAccessible(true);
    }

    @Override
    public void perform(Type.InstanceMap values) {
        try {
            method.invoke(instance, parameterConverter.convert(values));
        } catch(InvocationTargetException|IllegalAccessException e) {
            throw new HousemateException("Failed to perform command", e);
        }
    }

    private final class ParameterConverter {

        private final List<RealParameterImpl<?>> parameters;

        private ParameterConverter(List<RealParameterImpl<?>> parameters) {
            this.parameters = parameters;
        }

        public Object[] convert(Type.InstanceMap values) {
            Object[] result = new Object[parameters.size()];
            for(int i = 0; i < result.length; i++) {
                Type.Instances typeInstances = values.getChildren().get(parameters.get(i).getId());
                if(typeInstances == null || typeInstances.getElements().size() == 0)
                    result[i] = null;
                else
                    result[i] = parameters.get(i).getType().deserialise(typeInstances.getElements().get(0));
            }
            return result;
        }
    }

    public interface Factory {
        MethodCommand create(Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             List<RealParameterImpl<?>> parameters,
                             Method method,
                             Object instance);
    }
}
