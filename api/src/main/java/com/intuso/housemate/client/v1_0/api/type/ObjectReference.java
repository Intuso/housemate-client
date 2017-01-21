package com.intuso.housemate.client.v1_0.api.type;

import com.intuso.housemate.client.v1_0.api.object.Object;

/**
 * Reference for an object containing the object's path, and the object if it exists
 * @param <O>
 */
public class ObjectReference<O extends Object<?>> {

    private final String[] path;
    private O object;

    /**
     * @param path the path to the object
     */
    public ObjectReference(String[] path) {
        this(path, null);
    }

    /**
     * @param path the object's path
     * @param object the object
     */
    public ObjectReference(String[] path, O object) {
        this.path = path;
        this.object = object;
    }

    /**
     * Gets the path
     * @return the path
     */
    public String[] getPath() {
        return path;
    }

    /**
     * Gets the object
     * @return the object
     */
    public O getObject() {
        return object;
    }

    /**
     * Sets the object
     * @param object the object
     */
    public void setObject(O object) {
        // todo check the object's path matches the path inside this reference
        this.object = object;
    }
}
