package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.proxy.ChildUtil;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <ELEMENT> the type of the child
 */
public abstract class ProxyList<ELEMENT extends ProxyObject<?, ?>, LIST extends ProxyList<ELEMENT, ?>>
        extends ProxyObject<List.Data, List.Listener<? super ELEMENT, ? super LIST>>
        implements List<ELEMENT, LIST> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final ProxyObject.Factory<ELEMENT> elementFactory;

    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param elementFactory
     */
    public ProxyList(Logger logger, ManagedCollectionFactory managedCollectionFactory, Factory<ELEMENT> elementFactory) {
        super(logger, List.Data.class, managedCollectionFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String name, final Connection connection) throws JMSException {
        super.initChildren(name, connection);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, "*"), Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if (!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if (element != null) {
                                elements.put(data.getId(), element);
                                try {
                                    element.init(ChildUtil.name(name, data.getId()), connection);
                                } catch (JMSException e) {
                                    logger.error("Failed to init child {}", data.getId(), e);
                                }
                                for (List.Listener<? super ELEMENT, ? super LIST> listener : listeners)
                                    listener.elementAdded((LIST) ProxyList.this, element);
                                for(Map.Entry<ObjectReferenceImpl, Integer> reference : getMissingReferences(data.getId()).entrySet())
                                    reference(reference.getKey(), reference.getValue());
                            }
                        }
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        synchronized (elements) {
            for (ELEMENT ELEMENT : elements.values())
                ELEMENT.uninit();
            if (existingObjectReceiver != null) {
                existingObjectReceiver.close();
                existingObjectReceiver = null;
            }
        }
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

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded((LIST) this, element);
        return this.addObjectListener(listener);
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        return get(id);
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:16
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple<ELEMENT extends ProxyObject<?, ?>> extends ProxyList<ELEMENT, Simple<ELEMENT>> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Factory<ELEMENT> elementFactory) {
            super(logger, managedCollectionFactory, elementFactory);
        }
    }
}
