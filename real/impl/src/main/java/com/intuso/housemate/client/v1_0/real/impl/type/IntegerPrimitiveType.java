package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.IntegerPrimitiveSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerPrimitiveType extends RealPrimitiveType<Integer> {

    @Inject
    public IntegerPrimitiveType(@Type Logger logger,
                                ManagedCollectionFactory managedCollectionFactory,
                                Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, int.class.getName()),
                new PrimitiveData(int.class.getName(), "Integer", "A whole number"),
                IntegerPrimitiveSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
