package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
        DATA extends Object.Data,
        LISTENER extends Object.Listener,
        VIEW extends View>
        implements Object<DATA, LISTENER, VIEW> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    protected final String path;
    protected final String name;
    protected final Receiver.Factory receiverFactory;

    protected final ManagedCollection<LISTENER> listeners;
    private final ManagedCollection<Tree.Listener> treeListeners;

    protected DATA data = null;
    private Receiver<DATA> receiver;

    /**
     * @param logger the log
     * @param receiverFactory
     */
    protected ProxyObject(Logger logger, String path, String name, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory) {
        logger.debug("Creating {}", path);
        this.logger = logger;
        this.path = path;
        this.name = name;
        this.receiverFactory = receiverFactory;
        this.listeners = managedCollectionFactory.createSet();
        this.treeListeners = managedCollectionFactory.createSet();
        receiver = receiverFactory.create(logger, name, dataClass);
        receiver.listen(new Receiver.Listener<DATA>() {
            @Override
            public void onMessage(DATA data, boolean wasPersisted) {
                ProxyObject.this.data = data;
                ProxyObject.this.dataUpdated();
            }
        });
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

    public String getPath() {
        return path;
    }

    public void load(VIEW view) {}

    public void addTreeListener(VIEW view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {
        if(view != null && listener != null)
            listenerRegistrations.add(treeListeners.add(listener));
    }

    protected final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    protected final void dataUpdated() {
        for(Tree.Listener treeListener : treeListeners)
            treeListener.updated(path, new Tree(data));
    }

    public boolean isLoaded() {
        return data != null;
    }

    public interface Factory<OBJECT extends ProxyObject<?, ?, ?>> {
        OBJECT create(Logger logger, @Assisted("path") String path, @Assisted("name") String name);
    }
}
