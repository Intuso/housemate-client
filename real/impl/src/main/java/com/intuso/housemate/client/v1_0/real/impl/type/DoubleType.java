package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.DoubleSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoubleType extends RealPrimitiveType<Double> {

    @Inject
    public DoubleType(@Type Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, Double.class.getName()),
                new PrimitiveData("double", "Double", "A number with a decimal point"),
                DoubleSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
