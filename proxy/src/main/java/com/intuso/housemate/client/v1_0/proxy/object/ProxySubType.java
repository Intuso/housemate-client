package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.api.object.view.NoView;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<TYPE extends ProxyType<?>,
        SUB_TYPE extends ProxySubType<TYPE, SUB_TYPE>>
        extends ProxyObject<SubType.Data, SubType.Listener<? super SUB_TYPE>, NoView>
        implements SubType<TYPE, SUB_TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxySubType(Logger logger,
                        String name,
                        ManagedCollectionFactory managedCollectionFactory,
                        Receiver.Factory receiverFactory) {
        super(logger, name, SubType.Data.class, managedCollectionFactory, receiverFactory);
    }

    @Override
    public NoView createView(View.Mode mode) {
        return new NoView(mode);
    }

    @Override
    public Tree getTree(NoView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        return new Tree(getData());
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxySubType<ProxyType.Simple, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory);
        }
    }
}
