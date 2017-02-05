package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.util.Types;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.driver.*;
import com.intuso.housemate.client.v1_0.api.type.ObjectReference;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class TypeRepository implements TypeSerialiser.Repository {

    private final static Logger logger = LoggerFactory.getLogger(TypeRepository.class);

    private final Map<TypeSpec, RealTypeImpl<?>> types = Maps.newHashMap();

    @Inject
    public TypeRepository(// primitive types
                          BooleanType booleanType,
                          ByteType byteType,
                          DoubleType doubleType,
                          IntegerType integerType,
                          StringType stringType,
                          // regex types
                          EmailType emailType,
                          // objects
                          HardwareType hardwareType,
                          // factory types
                          ConditionDriverType conditionDriverType,
                          HardwareDriverType hardwareDriverType,
                          TaskDriverType taskDriverType) {
        typeAvailable(new TypeSpec(Boolean.class), booleanType);
        typeAvailable(new TypeSpec(boolean.class), booleanType);
        typeAvailable(new TypeSpec(Byte.class), byteType);
        typeAvailable(new TypeSpec(byte.class), byteType);
        typeAvailable(new TypeSpec(Double.class), doubleType);
        typeAvailable(new TypeSpec(double.class), doubleType);
        typeAvailable(new TypeSpec(Integer.class), integerType);
        typeAvailable(new TypeSpec(int.class), integerType);
        typeAvailable(new TypeSpec(String.class), stringType);
        typeAvailable(new TypeSpec(String.class, "email"), emailType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(ObjectReference.class, SimpleProxyHardware.class)), hardwareType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, ConditionDriver.class)), conditionDriverType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, HardwareDriver.class)), hardwareDriverType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, TaskDriver.class)), taskDriverType);
    }

    public <O> RealTypeImpl<O> getType(TypeSpec typeSpec) {
        if(!types.containsKey(typeSpec))
            throw new HousemateException("Unknown type: " + typeSpec.toString());
        return (RealTypeImpl<O>) types.get(typeSpec);
    }

    @Override
    public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
        return (TypeSerialiser<O>) types.get(typeSpec);
    }

    public synchronized void typeAvailable(TypeSpec typeSpec, RealTypeImpl<?> typeImpl) {
        logger.debug("Registering {} for spec {}", typeImpl.getClass().getName(), typeSpec);
        if(types.containsKey(typeSpec))
            throw new HousemateException("Duplicate type found when registering type " + typeImpl.getId());
        types.put(typeSpec, typeImpl);
    }

    public synchronized void typeUnavailable(TypeSpec typeSpec) {
        logger.debug("Unregistering spec {}", typeSpec);
        if(types.containsKey(typeSpec))
            types.remove(typeSpec);
    }
}
