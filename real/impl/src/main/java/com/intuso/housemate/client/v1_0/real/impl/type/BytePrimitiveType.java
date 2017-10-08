package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BytePrimitiveSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BytePrimitiveType extends RealPrimitiveType<Byte> {

    @Inject
    public BytePrimitiveType(@Type Logger logger,
                             ManagedCollectionFactory managedCollectionFactory,
                             Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, byte.class.getName()),
                new PrimitiveData(byte.class.getName(), "Byte", "A number between 0 and 255 inclusive"),
                BytePrimitiveSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
