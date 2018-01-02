package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.ValueBase;
import com.intuso.housemate.client.v1_0.api.object.view.TypeView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<TYPE extends ProxyType<TYPE>>
        extends ProxyObject<Type.Data, Type.Listener<? super TYPE>, TypeView>
        implements Type<TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyType(Logger logger,
                     String name,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory) {
        super(logger, name, Type.Data.class, managedCollectionFactory, receiverFactory);
    }

    @Override
    public TypeView createView(View.Mode mode) {
        return new TypeView(mode);
    }

    @Override
    public Tree getTree(TypeView view, ValueBase.Listener listener) {
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
    public static final class Simple extends ProxyType<Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory);
        }
    }
}
