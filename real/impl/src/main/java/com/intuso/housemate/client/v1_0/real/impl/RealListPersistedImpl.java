package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListPersistedImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>>>
        implements RealList<ELEMENT, RealListPersistedImpl<ELEMENT>> {

    private final Map<String, ELEMENT> elements;
    private final ExistingObjectFactory<ELEMENT> existingObjectHandler;

    private String name;
    private Connection connection;
    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealListPersistedImpl(@Assisted Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted ExistingObjectFactory<ELEMENT> existingObjectHandler,
                                 ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory);
        this.elements = Maps.newHashMap();
        this.existingObjectHandler = existingObjectHandler;
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener, boolean callForExistingElements) {
        ManagedCollection.Registration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(this, element);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.name = name;
        this.connection = connection;
        existingObjectReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, "*"), Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
            @Override
            public void onMessage(Object.Data data, boolean wasPersisted) {
                if(!elements.containsKey(data.getId())) {
                    ELEMENT element = existingObjectHandler.create(logger, data);
                    if(element != null)
                        add(element);
                }
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.connection = null;
        for(ELEMENT element : elements.values())
            element.uninit();
        if(existingObjectReceiver != null) {
            existingObjectReceiver.close();
            existingObjectReceiver = null;
        }
    }

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        if(connection != null) {
            try {
                element.init(ChildUtil.name(name, element.getId()), connection);
            } catch(JMSException e) {
                throw new HousemateException("Couldn't add element, failed to initialise it");
            }
        }
        for(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.get(id);
        if(element != null) {
            // todo delete the element's queues/topics
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListPersistedImpl<ELEMENT>> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ELEMENT get(String id) {
        return elements.get(id);
    }

    @Override
    public ELEMENT getByName(String name) {
        for (ELEMENT element : this)
            if (name.equalsIgnoreCase(element.getName()))
                return element;
        return null;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<ELEMENT extends RealObject<?, ?>> {
        RealListPersistedImpl<ELEMENT> create(Logger logger,
                                            @Assisted("id") String id,
                                            @Assisted("name") String name,
                                            @Assisted("description") String description,
                                            ExistingObjectFactory<ELEMENT> existingObjectFactory);
    }

    /**
     * Created by tomc on 23/06/16.
     */
    public interface ExistingObjectFactory<ELEMENT extends RealObject<?, ?>> {
        ELEMENT create(Logger parentLogger, Object.Data data);
    }
}
