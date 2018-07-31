package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.DoublePrimitiveSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoublePrimitiveType extends RealPrimitiveType<Double> {

    @Inject
    public DoublePrimitiveType(@Type Logger logger,
                               ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, double.class.getName()),
                new PrimitiveData(double.class.getName(), "Double", "A number with a decimal point"),
                DoublePrimitiveSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
