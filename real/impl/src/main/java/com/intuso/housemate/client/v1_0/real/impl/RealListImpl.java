package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListImpl<ELEMENT>>>
        implements RealList<ELEMENT, RealListImpl<ELEMENT>> {

    private final Map<String, ELEMENT> elements;
    private String name;
    private Session session;

    /**
     * @param logger {@inheritDoc}
     * @param elements the list's elements
     * @param listenersFactory
     */
    @Inject
    public RealListImpl(@Assisted Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        @Assisted Iterable<? extends ELEMENT> elements,
                        ListenersFactory listenersFactory) {
        super(logger, new List.Data(id, name, description), listenersFactory);
        this.elements = Maps.newHashMap();
        for(ELEMENT element : elements)
            this.elements.put(element.getId(), element);
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT, ? super RealListImpl<ELEMENT>> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(this, element);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        this.name = name;
        this.session = session;
        for(ELEMENT element : elements.values())
            element.init(ChildUtil.name(name, element.getId()), session);
    }

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        if(session != null) {
            try {
                element.init(ChildUtil.name(name, element.getId()), session);
            } catch(JMSException e) {
                throw new HousemateException("Couldn't add element, failed to initialise it");
            }
        }
        for(List.Listener<? super ELEMENT, ? super RealListImpl<ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        this.session = null;
        for(ELEMENT element : elements.values())
            element.uninit();
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.get(id);
        if(element != null) {
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListImpl<ELEMENT>> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ELEMENT get(String name) {
        return elements.get(name);
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
        RealListImpl<ELEMENT> create(Logger logger,
                                     @Assisted("id") String id,
                                     @Assisted("name") String name,
                                     @Assisted("description") String description,
                                     Iterable<? extends ELEMENT> elements);
    }
}
