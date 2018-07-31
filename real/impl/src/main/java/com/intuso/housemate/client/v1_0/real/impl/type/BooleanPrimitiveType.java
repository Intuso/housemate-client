package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanPrimitiveSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanPrimitiveType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanPrimitiveType(@Type Logger logger,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, boolean.class.getName()),
                new PrimitiveData(boolean.class.getName(), "Boolean", "True or false"),
                BooleanPrimitiveSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}