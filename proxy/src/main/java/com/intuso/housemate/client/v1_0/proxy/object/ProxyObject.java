package com.intuso.housemate.client.v1_0.proxy.object;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
        DATA extends Object.Data,
        LISTENER extends Object.Listener> implements Object<LISTENER> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    private final Class<DATA> dataClass;
    protected final Receiver.Factory receiverFactory;

    protected final ManagedCollection<LISTENER> listeners;

    protected DATA data = null;
    private Receiver<DATA> receiver;

    /**
     * @param logger the log
     * @param receiverFactory
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.dataClass = dataClass;
        this.receiverFactory = receiverFactory;
        this.listeners = managedCollectionFactory.create();
    }

    public DATA getData() {
        return data;
    }

    @Override
    public String getObjectClass() {
        return data == null ? null : data.getObjectClass();
    }

    @Override
    public String getId() {
        return data == null ? null : data.getId();
    }

    @Override
    public String getName() {
        return data == null ? null : data.getName();
    }

    @Override
    public String getDescription() {
        return data == null ? null : data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    protected final void init(String name) {
        logger.debug("Init {}", name);
        receiver = receiverFactory.create(logger, name, dataClass);
        receiver.listen(new Receiver.Listener<DATA>() {
            @Override
            public void onMessage(DATA data, boolean wasPersisted) {
                ProxyObject.this.data = data;
                dataUpdated();
            }
        });
        initChildren(name);
    }

    protected void initChildren(String name) {}

    protected final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    protected void dataUpdated() {}

    public boolean isLoaded() {
        return data != null;
    }

    public interface Factory<OBJECT extends ProxyObject<?, ?>> {
        OBJECT create(Logger logger);
    }
}
