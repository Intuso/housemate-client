package com.intuso.housemate.client.v1_0.proxy.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.ValueBase;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <DATA> the type of the data
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            DATA extends Object.Data,
            TYPE extends ProxyType<?>,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ProxyValueBase<DATA, TYPE, LISTENER, VALUE>>
        extends ProxyObject<DATA, LISTENER>
        implements ValueBase<Type.Instances, TYPE, LISTENER, VALUE> {

    private Receiver<Type.Instances> valueReceiver;

    private Type.Instances value;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValueBase(Logger logger,
                          Class<DATA> dataClass,
                          ManagedCollectionFactory managedCollectionFactory,
                          Receiver.Factory receiverFactory) {
        super(logger, dataClass, managedCollectionFactory, receiverFactory);
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        valueReceiver = receiverFactory.create(logger, ChildUtil.name(name, VALUE_ID), Type.Instances.class);
        value = valueReceiver.getMessage();
        valueReceiver.listen(new Receiver.Listener<Type.Instances>() {
            @Override
            public void onMessage(Type.Instances instances, boolean wasPersisted) {
                value = instances;
                // todo call object listeners
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueReceiver != null) {
            valueReceiver.close();
            valueReceiver = null;
        }
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValue() {
        return value;
    }
}
