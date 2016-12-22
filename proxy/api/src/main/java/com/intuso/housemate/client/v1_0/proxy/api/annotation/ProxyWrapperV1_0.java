package com.intuso.housemate.client.v1_0.proxy.api.annotation;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by tomc on 16/12/16.
 */
public class ProxyWrapperV1_0 implements ProxyWrapper {

    @Override
    public <T> T build(Logger logger, ProxyObject<?, ?> object, Class<T> clazz, String prefix) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new InvocationHandlerImpl(object));
    }

    private class InvocationHandlerImpl implements InvocationHandler {

        private final ProxyObject<?, ?> object;
        private final Map<Method, MethodInvocationHandler> handlers = Maps.newHashMap();

        private InvocationHandlerImpl(ProxyObject<?, ?> object) {
            this.object = object;
        }

        @Override
        public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    private interface MethodInvocationHandler {
        void handle(Object[] args);
    }

    private class DontKnowWhatToDoMethodInvocationHandler implements MethodInvocationHandler {

        private final Class<?> clazz;
        private final Method method;

        private DontKnowWhatToDoMethodInvocationHandler(Class<?> clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }

        @Override
        public void handle(Object[] args) {
            throw new HousemateException("Don't know how to handle invocation of " + clazz.getName() + " method " + method.toString() + ". Expecting one of the following annotations:\n" + Command.class);
        }
    }
}
