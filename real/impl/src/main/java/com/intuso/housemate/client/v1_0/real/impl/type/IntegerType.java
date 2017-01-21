package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.IntegerSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerType extends RealPrimitiveType<Integer> {

    @Inject
    public IntegerType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Integer.class.getName()),
                new PrimitiveData(Integer.class.getName(), "Integer", "A whole number"),
                IntegerSerialiser.INSTANCE,
                listenersFactory);
    }
}
