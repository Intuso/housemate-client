package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.DoubleObjectSerialiser;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoubleObjectType extends RealPrimitiveType<Double> {

    @Inject
    public DoubleObjectType(@Type Logger logger,
                            ManagedCollectionFactory managedCollectionFactory,
                            Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, Double.class.getName()),
                new PrimitiveData(Double.class.getName(), "Double", "A number with a decimal point"),
                DoubleObjectSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
