package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.type.serialiser.ByteObjectSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class ByteObjectType extends RealPrimitiveType<Byte> {

    @Inject
    public ByteObjectType(@Type Logger logger,
                          ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Byte.class.getName()),
                new PrimitiveData(Byte.class.getName(), "Byte", "A number between 0 and 255 inclusive"),
                ByteObjectSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
