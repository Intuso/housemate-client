package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SimpleTypeData;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for types that have a simple type, such as string, integer etc
 */
public abstract class RealSimpleType<O> extends RealTypeImpl<SimpleTypeData, NoChildrenData, O> {

    private final TypeSerialiser<O> serialiser;

    /**
     * @param log the log
     * @param listenersFactory
     * @param type the type of the simple type
     * @param serialiser the serialiser for the type
     */
    protected RealSimpleType(Log log, ListenersFactory listenersFactory, SimpleTypeData.Type type, TypeSerialiser<O> serialiser) {
        super(log, listenersFactory, new SimpleTypeData(type));
        this.serialiser = serialiser;
    }

    @Override
    public TypeInstance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public O deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }
}
