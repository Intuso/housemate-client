package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.client.v1_0.real.impl.*;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationProcessor {

    private final RealListImpl<RealTypeImpl<?>> types;
    private final MethodCommand.Factory commandFactory;
    private final RealParameter.Factory<RealParameterImpl<?>> parameterFactory;
    private final FieldProperty.Factory fieldPropertyFactory;
    private final MethodProperty.Factory methodPropertyFactory;
    private final RealValue.Factory<RealValueImpl<?>> valueFactory;

    @Inject
    public AnnotationProcessor(RealListImpl<RealTypeImpl<?>> types, MethodCommand.Factory commandFactory,
                               RealParameter.Factory<RealParameterImpl<?>> parameterFactory,
                               FieldProperty.Factory fieldPropertyFactory, MethodProperty.Factory methodPropertyFactory,
                               RealValue.Factory<RealValueImpl<?>> valueFactory) {
        this.types = types;
        this.commandFactory = commandFactory;
        this.parameterFactory = parameterFactory;
        this.fieldPropertyFactory = fieldPropertyFactory;
        this.methodPropertyFactory = methodPropertyFactory;
        this.valueFactory = valueFactory;
    }

    public Iterable<RealCommand> findCommands(Logger logger, Object object) {
        return findCommands(logger, object, object.getClass());
    }

    public Iterable<RealCommand> findCommands(Logger logger, Object object, Class<?> clazz) {
        return findV1_0Commands(logger, object, clazz);
    }

    private Iterable<RealCommand> findV1_0Commands(Logger logger, Object object, Class<?> clazz) {
        List<RealCommand> commands = Lists.newArrayList();
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Command> commandMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Command.class).entrySet()) {
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = commandMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on command method " + commandMethod.getKey().getName() + " of class " + clazz);
            commands.add(commandFactory.create(ChildUtil.logger(logger, typeInfo.id()),
                    typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    parseV1_0Parameters(logger, clazz, commandMethod.getKey()), commandMethod.getKey(), object));
        }
        return commands;
    }

    private List<RealParameterImpl<?>> parseV1_0Parameters(Logger logger, Class<?> clazz, Method method) {
        List<RealParameterImpl<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            com.intuso.housemate.client.v1_0.real.api.annotations.Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], com.intuso.housemate.client.v1_0.real.api.annotations.Parameter.class);
            if(parameterAnnotation == null)
                throw new HousemateException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + com.intuso.housemate.client.v1_0.real.api.annotations.Parameter.class.getName());
            if(types.get(parameterAnnotation.value()) == null)
                throw new HousemateException(parameterAnnotation.value() + " type does not exist");
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = getAnnotation(parameterAnnotations[a], com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on parameter " + a + " of command method " + method.getName() + " of class " + clazz);
            result.add(parameterFactory.create(ChildUtil.logger(logger, typeInfo.id()), typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(parameterAnnotation.value())));
        }
        return result;
    }

    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object) {
        return findProperties(logger, object, object.getClass());
    }

    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object, Class<?> clazz) {
        return findV1_0Properties(logger, object, clazz);
    }

    private Iterable<RealPropertyImpl<?>> findV1_0Properties(Logger logger, Object object, Class<?> clazz) {
        List<RealPropertyImpl<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, com.intuso.housemate.client.v1_0.real.api.annotations.Property> propertyField : getAnnotatedFields(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(object);
            } catch(IllegalAccessException e) {
                logger.warn("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().value()) == null)
                throw new HousemateException(propertyField.getValue().value() + " type does not exist");
            RealTypeImpl<Object> type = (RealTypeImpl<Object>) types.get(propertyField.getValue().value());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new Type.Instance(propertyField.getValue().initialValue()));
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = propertyField.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on property field" + propertyField.getKey().getName() + " of class " + clazz);
            properties.add(fieldPropertyFactory.create(ChildUtil.logger(logger, typeInfo.id()),
                    typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyField.getKey(), object));
        }
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Property> propertyMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().value()) == null)
                throw new HousemateException(propertyMethod.getValue().value() + " type does not exist");
            RealTypeImpl<Object> type = (RealTypeImpl<Object>) types.get(propertyMethod.getValue().value());
            Object value = getV1_0InitialValue(logger, object, clazz, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = propertyMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            if(typeInfo == null)
                throw new HousemateException("No " + com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class.getName() + " on property field" + propertyMethod.getKey().getName() + " of class " + clazz);
            properties.add(methodPropertyFactory.create(ChildUtil.logger(logger, typeInfo.id()),
                    typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyMethod.getKey(), object));
        }
        return properties;
    }

    private Object getV1_0InitialValue(Logger logger, Object object, Class<?> clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Property property, RealTypeImpl<Object> type, String methodName) {
        if(property.initialValue().length() > 0)
            return type.deserialise(new Type.Instance(property.initialValue()));
        else if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = clazz.getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using getter " + getterName + " of " + clazz.getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = clazz.getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                logger.error("Problem getting property initial value using isGetter " + isGetterName + " of " + clazz.getName());
            }
        }
        logger.warn("No initial value or getter found for " + Property.class.getSimpleName() + " method " + methodName + " of " + clazz.getName());
        return null;
    }

    private List<RealValueImpl<?>> findValues(Logger logger, Object object, Class<?> clazz) {
        for(Class<?> interfaceClass : clazz.getClasses()) {
            if (interfaceClass.getAnnotation(Values.class) != null)
                return findObjectValues(logger, object, object.getClass(), interfaceClass);
            else if(interfaceClass.getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.Values.class) != null)
                return findObjectValues(logger, object, object.getClass(), interfaceClass);
        }
        return Lists.newArrayList();
    }

    private List<RealValueImpl<?>> findObjectValues(Logger logger, Object object, Class<?> lookInClass, Class<?> valuesClass) {
        for(Field field : lookInClass.getDeclaredFields()) {
            if(valuesClass.isAssignableFrom(field.getType())) {
                return getValuesImpl(logger, object, field, valuesClass);
            }
        }
        if(lookInClass.getSuperclass() != null)
            return findObjectValues(logger, object, lookInClass.getSuperclass(), valuesClass);
        return Lists.newArrayList();
    }

    private List<RealValueImpl<?>> getValuesImpl(Logger logger, Object object, Field field, Class<?> clazz) {
        List<RealValueImpl<?>> values = Lists.newArrayList();
        Map<Method, RealValueImpl<?>> valuesFunctions = Maps.newHashMap();
        InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
        try {
            field.setAccessible(true);
            field.set(object, instance);
        } catch(IllegalAccessException e) {
            throw new HousemateException("Failed to assign proxy instance to field " + field.getName() + " of class " + object.getClass().getName(), e);
        }
        findV1_0Values(logger, values, valuesFunctions, clazz);
        return values;
    }

    private void findV1_0Values(Logger logger, List<RealValueImpl<?>> values, Map<Method, RealValueImpl<?>> valuesFunctions, Class<?> clazz) {
        for(Map.Entry<Method, com.intuso.housemate.client.v1_0.real.api.annotations.Value> valueMethod : getAnnotatedMethods(clazz, com.intuso.housemate.client.v1_0.real.api.annotations.Value.class).entrySet()) {
            if(types.get(valueMethod.getValue().value()) == null)
                throw new HousemateException(valueMethod.getValue().value() + " type does not exist");
            com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo typeInfo = valueMethod.getKey().getAnnotation(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            RealValueImpl<?> value = valueFactory.create(ChildUtil.logger(logger, typeInfo.id()), typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(valueMethod.getValue().value()));
            valuesFunctions.put(valueMethod.getKey(), value);
            values.add(value);
        }
    }

    private <A extends Annotation> Map<Method, A> getAnnotatedMethods(Class<?> objectClass, Class<A> annotationClass) {
        Map<Method, A> result = Maps.newHashMap();
        getAnnotatedMethods(objectClass, annotationClass, result);
        return result;
    }

    private <A extends Annotation> void getAnnotatedMethods(Class<?> objectClass, Class<A> annotationClass,
                                                            Map<Method, A> methods) {
        for(Method method : objectClass.getDeclaredMethods())
            if(method.getAnnotation(annotationClass) != null)
                methods.put(method, method.getAnnotation(annotationClass));
        for(Class<?> interfaceClass : objectClass.getInterfaces())
            getAnnotatedMethods(interfaceClass, annotationClass, methods);
        if(objectClass.getSuperclass() != null)
            getAnnotatedMethods(objectClass.getSuperclass(), annotationClass, methods);
    }

    private <A extends Annotation> Map<Field, A> getAnnotatedFields(Class<?> objectClass, Class<A> annotationClass) {
        Map<Field, A> result = Maps.newHashMap();
        getAnnotatedFields(objectClass, annotationClass, result);
        return result;
    }

    private <A extends Annotation> void getAnnotatedFields(Class<?> objectClass, Class<A> annotationClass,
                                                           Map<Field, A> fields) {
        for(Field field : objectClass.getDeclaredFields())
            if(field.getAnnotation(annotationClass) != null)
                fields.put(field, field.getAnnotation(annotationClass));
        if(objectClass.getSuperclass() != null)
            getAnnotatedFields(objectClass.getSuperclass(), annotationClass, fields);
    }

    private <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> annotationClass) {
        for(Annotation annotation : annotations)
            if(annotation.annotationType().equals(annotationClass))
                return (A)annotation;
        return null;
    }
}
