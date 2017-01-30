package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.ValueBase;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

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

    private JMSUtil.Receiver<Type.Instances> valueReceiver;

    private Type.Instances value;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValueBase(Logger logger,
                          Class<DATA> dataClass,
                          ManagedCollectionFactory managedCollectionFactory) {
        super(logger, dataClass, managedCollectionFactory);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        value = JMSUtil.getPersisted(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, VALUE_ID), Type.Instances.class);
        valueReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, VALUE_ID), Type.Instances.class,
                new JMSUtil.Receiver.Listener<Type.Instances>() {
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
