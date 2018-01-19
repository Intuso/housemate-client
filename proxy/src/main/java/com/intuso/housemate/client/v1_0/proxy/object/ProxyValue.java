package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.api.object.view.ValueView;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValue<
        TYPE extends ProxyType<?>,
        VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<Value.Data, Value.Listener<? super VALUE>, ValueView, TYPE, VALUE>
        implements Value<Type.Instances, TYPE, VALUE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValue(Logger logger,
                      String path,
                      String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
        super(logger, path, name, Value.Data.class, managedCollectionFactory, receiverFactory);
    }

    @Override
    public ValueView createView(View.Mode mode) {
        return new ValueView(mode);
    }

    @Override
    public Tree getTree(ValueView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        return new Tree(getData());
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
    public static final class Simple extends ProxyValue<ProxyType.Simple, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory);
        }
    }
}
