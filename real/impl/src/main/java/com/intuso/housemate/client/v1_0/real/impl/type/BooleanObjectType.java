package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanObjectSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanObjectType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanObjectType(@Type Logger logger,
                             ManagedCollectionFactory managedCollectionFactory,
                             Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, Boolean.class.getName()),
                new PrimitiveData(Boolean.class.getName(), "Boolean", "True or false"),
                BooleanObjectSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}