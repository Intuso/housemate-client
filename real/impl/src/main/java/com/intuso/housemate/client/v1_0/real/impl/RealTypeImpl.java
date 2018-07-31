package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.view.TypeView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the type instances
 */
public abstract class RealTypeImpl<O>
        extends RealObject<Type.Data, Type.Listener<? super RealTypeImpl<O>>, TypeView>
        implements RealType<O, RealTypeImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     * @param managedCollectionFactory {@inheritDoc}
     */
    protected RealTypeImpl(Logger logger,
                           Type.Data data,
                           ManagedCollectionFactory managedCollectionFactory) {
        super(logger, data, managedCollectionFactory);
    }

    @Override
    public TypeView createView(View.Mode mode) {
        return new TypeView(mode);
    }

    @Override
    public Tree getTree(TypeView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        return new Tree(getData());
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return null;
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, O ... typedValues) {
        return serialiseAll(serialiser, Arrays.asList(typedValues));
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, Iterable<O> typedValues) {
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
