package com.intuso.housemate.client.v1_0.proxy.api.object.type.serialiser;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.*;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class SystemTypeSerialisersRepository implements TypeSerialiser.Repository {

    private final Map<TypeSpec, TypeSerialiser<?>> serialisers = Maps.newHashMap();

    @Inject
    public SystemTypeSerialisersRepository(// primitive types
                                           BooleanSerialiser booleanSerialiser,
                                           DoubleSerialiser doubleSerialiser,
                                           IntegerSerialiser integerSerialiser,
                                           StringSerialiser stringSerialiser) {
        serialisers.put(new TypeSpec(Boolean.class), booleanSerialiser);
        serialisers.put(new TypeSpec(Double.class), doubleSerialiser);
        serialisers.put(new TypeSpec(Integer.class), integerSerialiser);
        serialisers.put(new TypeSpec(String.class), stringSerialiser);
        serialisers.put(new TypeSpec(String.class, "email"), stringSerialiser);
    }

    @Override
    public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
        return (TypeSerialiser<O>) serialisers.get(typeSpec);
    }
}
