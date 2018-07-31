package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.view.NoView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.real.api.RealSubType;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the sub type's value
 */
public final class RealSubTypeImpl<O>
        extends RealObject<SubType.Data, SubType.Listener<? super RealSubTypeImpl<O>>, NoView>
        implements RealSubType<O, RealTypeImpl<O>, RealSubTypeImpl<O>> {

    private final RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type
     */
    @Inject
    public RealSubTypeImpl(@Assisted Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           @Assisted RealTypeImpl type,
                           @Assisted("min") int minValues,
                           @Assisted("max") int maxValues,
                           ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new SubType.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory);
        this.type = type;
    }

    @Override
    public NoView createView(View.Mode mode) {
        return new NoView(mode);
    }

    @Override
    public Tree getTree(NoView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        return new Tree(getData());
    }

    @Override
    public final RealTypeImpl<O> getType() {
        return type;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return null;
    }

    public interface Factory {
        RealSubTypeImpl<?> create(Logger logger,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  RealTypeImpl type,
                                  @Assisted("min") int minValues,
                                  @Assisted("max") int maxValues);
    }
}
