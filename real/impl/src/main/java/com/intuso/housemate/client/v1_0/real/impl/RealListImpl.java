package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListImpl<ELEMENT extends RealObject<?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT>>
        implements RealList<ELEMENT> {

    private final Map<String, ELEMENT> elements;

    /**
     * @param logger {@inheritDoc}
     * @param data
     * @param listenersFactory
     * @param elements the list's elements
     */
    public RealListImpl(Logger logger, List.Data data, ListenersFactory listenersFactory, ELEMENT ... elements) {
        this(logger, data, listenersFactory, Lists.newArrayList(elements));
    }

    /**
     * @param logger {@inheritDoc}
     * @param data
     * @param listenersFactory
     * @param elements the list's elements
     */
    public RealListImpl(Logger logger, List.Data data, ListenersFactory listenersFactory, java.util.List<ELEMENT> elements) {
        super(logger, data, listenersFactory);
        this.elements = Maps.uniqueIndex(elements, new Function<ELEMENT, String>() {
            @Nullable
            @Override
            public String apply(ELEMENT element) {
                return element.getId();
            }
        });
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        for(List.Listener<? super ELEMENT> listener : listeners)
            listener.elementAdded(element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.get(id);
        if(element != null)
            for(List.Listener<? super ELEMENT> listener : listeners)
                listener.elementRemoved(element);
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
}
