package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.List;
import java.util.Map;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
        DATA extends Object.Data,
        LISTENER extends Object.Listener> implements Object<LISTENER> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    protected final ManagedCollection<LISTENER> listeners;
    private final Class<DATA> dataClass;

    private final List<ObjectReferenceImpl> references = Lists.newArrayList();
    private final Map<String, Map<ObjectReferenceImpl, Integer>> missingReferences = Maps.newHashMap();

    protected DATA data = null;
    private JMSUtil.Receiver<DATA> receiver;

    /**
     * @param logger the log
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.dataClass = dataClass;
        this.listeners = managedCollectionFactory.create();
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

    protected final void init(String name, Connection connection) throws JMSException {
        logger.debug("Init {}", name);
        receiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, name, dataClass,
                new JMSUtil.Receiver.Listener<DATA>() {
                    @Override
                    public void onMessage(DATA data, boolean wasPersisted) {
                        ProxyObject.this.data = data;
                        dataUpdated();
                    }
                });
        initChildren(name, connection);
    }

    protected void initChildren(String name, Connection connection) throws JMSException {}

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

    public abstract ProxyObject<?, ?> getChild(String id);

    protected final void manageReference(ObjectReferenceImpl reference, int pathIndex) {
        if(pathIndex == reference.getPath().length) {
            references.add(reference);
            reference.setObject(this);
        } else {
            String id = reference.getPath()[pathIndex];
            ProxyObject<?, ?> child = getChild(id);
            if(child != null)
                child.manageReference(reference, pathIndex + 1);
            else {
                if(!missingReferences.containsKey(id))
                    missingReferences.put(id, Maps.<ObjectReferenceImpl, Integer>newHashMap());
                missingReferences.get(id).put(reference, pathIndex);
            }
        }
    }

    protected Map<ObjectReferenceImpl, Integer> getMissingReferences(String id) {
        return missingReferences.containsKey(id) ? missingReferences.remove(id) : Maps.<ObjectReferenceImpl, Integer>newHashMap();
    }

    public interface Factory<OBJECT extends ProxyObject<?, ?>> {
        OBJECT create(Logger logger);
    }
}
