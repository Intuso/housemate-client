package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.ValueBase;
import com.intuso.housemate.client.v1_0.api.object.view.ListView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListGeneratedImpl<ELEMENT extends RealObject<?, ?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>>, ListView<?>>
        implements RealList<ELEMENT, RealListGeneratedImpl<ELEMENT>> {

    private final Map<String, ELEMENT> elements;

    private String name;

    /**
     * @param logger {@inheritDoc}
     * @param elements the list's elements
     * @param managedCollectionFactory
     */
    @Inject
    public RealListGeneratedImpl(@Assisted Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted Iterable<? extends ELEMENT> elements,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 Sender.Factory senderFactory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.elements = Maps.newHashMap();
        for(ELEMENT element : elements)
            this.elements.put(element.getId(), element);
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view, ValueBase.Listener listener) {

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((RealObject) element.getValue()).getTree(element.getValue().createView(View.Mode.ANCESTORS), listener));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((RealObject) element.getValue()).getTree(view.getView(), listener));
                    break;

                case SELECTION:
                    if(view.getElements() != null)
                        for (String elementId : view.getElements())
                            if (elements.containsKey(elementId))
                                result.getChildren().put(elementId, ((RealObject) elements.get(elementId)).getTree(view.getView(), listener));
                    break;
            }

        }

        return result;
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener, boolean callForExistingElements) {
        ManagedCollection.Registration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(this, element);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        this.name = name;
        for(ELEMENT element : elements.values())
            element.init(ChildUtil.name(name, element.getId()));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        for(ELEMENT element : elements.values())
            element.uninit();
    }

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        element.init(ChildUtil.name(name, element.getId()));
        for(List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.remove(id);
        if(element != null) {
            // todo delete the element's queues/topics
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListGeneratedImpl<ELEMENT>> listener : listeners)
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
    public RealObject<?, ?, ?> getChild(String id) {
        return get(id);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<ELEMENT extends RealObject<?, ?, ?>> {
        RealListGeneratedImpl<ELEMENT> create(Logger logger,
                                     @Assisted("id") String id,
                                     @Assisted("name") String name,
                                     @Assisted("description") String description,
                                     Iterable<? extends ELEMENT> elements);
    }
}
