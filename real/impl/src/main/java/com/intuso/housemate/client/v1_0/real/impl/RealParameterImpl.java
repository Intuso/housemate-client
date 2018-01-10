package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.view.ParameterView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the parameter's value
 */
public final class RealParameterImpl<O>
        extends RealObject<Parameter.Data, Parameter.Listener<? super RealParameterImpl<O>>, ParameterView>
        implements RealParameter<O, RealTypeImpl<O>, RealParameterImpl<O>> {

    private RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type the type of the parameter's value
     */
    @Inject
    public RealParameterImpl(@Assisted final Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted RealTypeImpl type,
                             @Assisted("min") int minValues,
                             @Assisted("max") int maxValues,
                             ManagedCollectionFactory managedCollectionFactory,
                             Sender.Factory senderFactory) {
        super(logger, new Parameter.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory, senderFactory);
        this.type = type;
    }

    @Override
    public ParameterView createView(View.Mode mode) {
        return new ParameterView(mode);
    }

    @Override
    public Tree getTree(ParameterView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

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
        RealParameterImpl<?> create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    RealTypeImpl type,
                                    @Assisted("min") int minValues,
                                    @Assisted("max") int maxValues);
    }
}
