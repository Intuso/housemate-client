package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.ByteSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class ByteType extends RealPrimitiveType<Byte> {

    @Inject
    public ByteType(@Type Logger logger,
                    ManagedCollectionFactory managedCollectionFactory,
                    Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, Byte.class.getName()),
                new PrimitiveData(Byte.class.getName(), "Byte", "A number between 0 and 7 inclusive"),
                ByteSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
