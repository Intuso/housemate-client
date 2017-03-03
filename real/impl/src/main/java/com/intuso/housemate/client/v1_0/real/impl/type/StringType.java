package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.StringSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a string
 */
public class StringType extends RealPrimitiveType<String> {

    @Inject
    public StringType(@Type Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, String.class.getName()),
                new PrimitiveData("string", "String", "Some text"),
                StringSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
