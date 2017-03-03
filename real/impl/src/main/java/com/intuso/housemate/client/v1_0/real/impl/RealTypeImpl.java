package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the type instances
 */
public abstract class RealTypeImpl<O>
        extends RealObject<Type.Data, Type.Listener<? super RealTypeImpl<O>>>
        implements RealType<O, RealTypeImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     * @param managedCollectionFactory {@inheritDoc}
     */
    protected RealTypeImpl(Logger logger,
                           Type.Data data,
                           ManagedCollectionFactory managedCollectionFactory,
                           Sender.Factory senderFactory) {
        super(logger, data, managedCollectionFactory, senderFactory);
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, O ... typedValues) {
        return serialiseAll(serialiser, Arrays.asList(typedValues));
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, List<O> typedValues) {
        if(typedValues == null)
            return null;
        Instances result = new Instances();
        for(O typedValue : typedValues)
            result.getElements().add(serialiser.serialise(typedValue));
        return result;
    }

    public static <O> List<O> deserialiseAll(TypeSerialiser<O> serialiser, Instances values) {
        if(values == null)
            return null;
        List<O> result = Lists.newArrayList();
        for(Instance value : values.getElements())
            result.add(serialiser.deserialise(value));
        return result;
    }
}
