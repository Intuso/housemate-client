package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.utilities.listener.Listener;

/**
 * Listener for when a child object was loaded
 * @param <OBJECT> the object type
 * @param <CHILD> the child type
 */
public interface ChildLoadedListener<
            OBJECT extends ProxyObject<?, ?, CHILD, ?, ?>,
            CHILD extends ProxyObject<?, ?, ?, ?, ?>>
        extends Listener {

    /**
     * Callback method for when a child object was loaded
     * @param object the object that was loaded
     * @param child the child that the object was loaded in to
     */
    public void childLoaded(OBJECT object, CHILD child);
}
