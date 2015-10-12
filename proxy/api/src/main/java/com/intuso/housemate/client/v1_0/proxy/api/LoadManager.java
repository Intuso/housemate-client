package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.collect.Lists;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;

import java.util.List;

/**
 * Base class for managing the loading of remote objects
 */
public class LoadManager {

    private final Callback callback;
    private final List<TreeLoadInfo> toLoad;

    public LoadManager(Callback callback, TreeLoadInfo... toLoad) {
        this(callback, Lists.newArrayList(toLoad));
    }

    public LoadManager(Callback callback, List<TreeLoadInfo> toLoad) {
        this.callback = callback;
        this.toLoad = toLoad;
    }

    /**
     * Get the info of the objects to load
     * @return the info of the objects to load
     */
    public final List<TreeLoadInfo> getToLoad() {
        return toLoad;
    }

    /**
     * Callback for when an object's load has finished
     */
    protected final void succeeded() {
        callback.succeeded();
    }

    protected final void failed(List<String> errors) {
        callback.failed(errors);
    }

    public static interface Callback {

        /**
         * Callback for when the load of some objects failed
         * @param errors
         */
        public void failed(List<String> errors);

        /**
         * Callback for when all required objects have been loaded
         */
        public void succeeded();
    }
}
