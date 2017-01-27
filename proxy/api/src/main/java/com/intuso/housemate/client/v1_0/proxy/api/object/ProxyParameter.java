package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<TYPE extends ProxyType<?>,
            PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<Parameter.Data, Parameter.Listener<? super PARAMETER>>
        implements Parameter<TYPE, PARAMETER> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyParameter(Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Parameter.Data.class, managedCollectionFactory);
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        return null;
    }
}
