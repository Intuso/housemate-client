package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<TYPE extends ProxyType<TYPE>>
        extends ProxyObject<Type.Data, Type.Listener<? super TYPE>>
        implements Type<TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyType(Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Type.Data.class, managedCollectionFactory);
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
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
                      ManagedCollectionFactory managedCollectionFactory) {
            super(logger, managedCollectionFactory);
        }
    }
}
