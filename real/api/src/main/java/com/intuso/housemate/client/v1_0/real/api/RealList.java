package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.List;

/**
 */
public interface RealList<CHILD>
        extends List<CHILD> {

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    void add(CHILD element);

    /**
     * Removes an elements from the list
     * @param id the id of the element to remove
     * @return the removed element, or null if there was none for the id
     */
    CHILD remove(String id);
}
