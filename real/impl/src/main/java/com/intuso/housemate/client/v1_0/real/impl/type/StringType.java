package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.StringSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a string
 */
public class StringType extends RealPrimitiveType<String> {

    @Inject
    public StringType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, String.class.getName()),
                new PrimitiveData(String.class.getName(), "String", "Some text"),
                new TypeSpec(String.class),
                StringSerialiser.INSTANCE,
                listenersFactory);
    }
}
