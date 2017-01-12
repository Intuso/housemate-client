package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.DoubleSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoubleType extends RealPrimitiveType<Double> {

    @Inject
    public DoubleType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Double.class.getName()),
                new PrimitiveData(Double.class.getName(), "Double", "A number with a decimal point"),
                new TypeSpec(Double.class),
                DoubleSerialiser.INSTANCE,
                listenersFactory);
    }
}
