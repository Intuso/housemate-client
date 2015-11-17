package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.api.annotations.*;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.utilities.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * Processor of annotated devices etc
 */
public class AnnotationProcessor {

    private final Log log;
    private final RealList<RealType<?>> types;
    private final MethodCommand.Factory commandFactory;
    private final RealParameter.Factory parameterFactory;
    private final FieldProperty.Factory fieldPropertyFactory;
    private final MethodProperty.Factory methodPropertyFactory;
    private final RealValue.Factory valueFactory;

    @Inject
    public AnnotationProcessor(Log log, RealList<RealType<?>> types, MethodCommand.Factory commandFactory, RealParameter.Factory parameterFactory, FieldProperty.Factory fieldPropertyFactory, MethodProperty.Factory methodPropertyFactory, RealValue.Factory valueFactory) {
        this.log = log;
        this.types = types;
        this.commandFactory = commandFactory;
        this.parameterFactory = parameterFactory;
        this.fieldPropertyFactory = fieldPropertyFactory;
        this.methodPropertyFactory = methodPropertyFactory;
        this.valueFactory = valueFactory;
    }

    public Iterable<RealCommand> findCommands(Object driver) {
        List<RealCommand> commands = Lists.newArrayList();
        for(Map.Entry<Method, Command> commandMethod : getAnnotatedMethods(driver.getClass(), Command.class).entrySet()) {
            TypeInfo typeInfo = commandMethod.getKey().getAnnotation(TypeInfo.class);
            commands.add(commandFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    parseParameters(commandMethod.getKey()), commandMethod.getKey(), driver));
        }
        return commands;
    }

    private List<RealParameter<?>> parseParameters(Method method) {
        List<RealParameter<?>> result = Lists.newArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int a = 0; a < parameterAnnotations.length; a++) {
            Parameter parameterAnnotation = getAnnotation(parameterAnnotations[a], Parameter.class);
            TypeInfo typeInfo = getAnnotation(parameterAnnotations[a], TypeInfo.class);
            if(parameterAnnotation == null)
                throw new HousemateCommsException("Parameter " + a + " of command method " + method.getName()
                        + " is not annotated with " + Parameter.class.getName());
            if(types.get(parameterAnnotation.value()) == null)
                throw new HousemateCommsException(parameterAnnotation.value() + " type does not exist");
            result.add(parameterFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    types.get(parameterAnnotation.value())));
        }
        return result;
    }

    public Iterable<RealValue<?>> findValues(Object driver) {
        List<RealValue<?>> values = Lists.newArrayList();
        for(Map.Entry<Field, Values> valuesField : getAnnotatedFields(driver.getClass(), Values.class).entrySet()) {
            Map<Method, RealValue<?>> valuesFunctions = Maps.newHashMap();
            InvocationHandler invocationHandler = new ValuesInvocationHandler(valuesFunctions);
            Object instance = Proxy.newProxyInstance(valuesField.getKey().getType().getClassLoader(),
                    new Class<?>[] {valuesField.getKey().getType()},
                    invocationHandler);
            try {
                valuesField.getKey().set(driver, instance);
            } catch(IllegalAccessException e) {
                throw new HousemateCommsException("Failed to assign proxy instance to " + valuesField.getKey().getName());
            }
            for(Map.Entry<Method, Value> valueMethod : getAnnotatedMethods(valuesField.getKey().getType(), Value.class).entrySet()) {
                if(types.get(valueMethod.getValue().value()) == null)
                    throw new HousemateCommsException(valueMethod.getValue().value() + " type does not exist");
                TypeInfo typeInfo = valueMethod.getKey().getAnnotation(TypeInfo.class);
                RealValue<?> value = valueFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                        types.get(valueMethod.getValue().value()), null);
                valuesFunctions.put(valueMethod.getKey(), value);
                values.add(value);
            }
        }
        return values;
    }

    public Iterable<RealProperty<?>> findProperties(Object driver) {
        List<RealProperty<?>> properties = Lists.newArrayList();
        for(Map.Entry<Field, Property> propertyField : getAnnotatedFields(driver.getClass(), Property.class).entrySet()) {
            Object value = null;
            try {
                value = propertyField.getKey().get(driver);
            } catch(IllegalAccessException e) {
                log.w("Failed to get initial value of annotated property field " + propertyField.getKey().getName());
            }
            if(types.get(propertyField.getValue().value()) == null)
                throw new HousemateCommsException(propertyField.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyField.getValue().value());
            if(value == null && propertyField.getValue().initialValue().length() > 0)
                value = type.deserialise(new TypeInstance(propertyField.getValue().initialValue()));
            TypeInfo typeInfo = propertyField.getKey().getAnnotation(TypeInfo.class);
            properties.add(fieldPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyField.getKey(), driver));
        }
        for(Map.Entry<Method, Property> propertyMethod : getAnnotatedMethods(driver.getClass(), Property.class).entrySet()) {
            if(propertyMethod.getKey().getParameterTypes().length != 1)
                throw new HousemateCommsException(propertyMethod.getKey().getName() + " must take a single argument");
            if(types.get(propertyMethod.getValue().value()) == null)
                throw new HousemateCommsException(propertyMethod.getValue().value() + " type does not exist");
            RealTypeImpl<?, ?, Object> type = (RealTypeImpl<?, ?, Object>) types.get(propertyMethod.getValue().value());
            Object value = getInitialValue(driver, propertyMethod.getValue(), type, propertyMethod.getKey().getName());
            TypeInfo typeInfo = propertyMethod.getKey().getAnnotation(TypeInfo.class);
            properties.add(methodPropertyFactory.create(typeInfo.id(), typeInfo.name(), typeInfo.description(),
                    type, value, propertyMethod.getKey(), driver));
        }
        return properties;
    }

    private Object getInitialValue(Object object, Property property, RealTypeImpl<?, ?, Object> type, String methodName) {
        if(property.initialValue().length() > 0)
            return type.deserialise(new TypeInstance(property.initialValue()));
        else if(methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            String getterName = "get" + fieldName;
            try {
                Method getter = object.getClass().getMethod(getterName);
                return getter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using getter " + getterName + " of " + object.getClass().getName());
            }
            String isGetterName = "is" + fieldName;
            try {
                Method isGetter = object.getClass().getMethod(isGetterName);
                return isGetter.invoke(object);
            } catch(NoSuchMethodException e) { // do nothing
            } catch(InvocationTargetException|IllegalAccessException e) {
                log.e("Problem getting property initial value using isGetter " + isGetterName + " of " + object.getClass().getName());
            }
        }
        log.w("No initial value or getter found for " + Property.class.getSimpleName() + " method " + methodName + " of " + object.getClass().getName());
        return null;
    }

    public Iterable<RealFeature> findFeatures(Object driver) {
        List<RealFeature> features = Lists.newArrayList();
        /*for(Class<?> interfaceClass : driverClass.getInterfaces())
            if(RealFeature.class.isAssignableFrom(interfaceClass))
                addFeatureId(device, interfaceClass);
        if(RealDevice.class.isAssignableFrom(driverClass.getSuperclass()))
            parseFeatures(device, driverClass.getSuperclass());*/
        return features;
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
