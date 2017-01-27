package com.intuso.housemate.client.v1_0.api.driver;

import com.intuso.utilities.collection.ManagedCollection;

/**
 * Created by tomc on 29/10/15.
 */
public interface PluginDependency<DEPENDENCY> {

    DEPENDENCY getDependency();

    ManagedCollection.Registration addListener(Listener<DEPENDENCY> listener);

    interface Listener<DEPENDENCY> {
        void dependencyAvailable(DEPENDENCY DEPENDENCY);
        void dependencyUnavailable();
    }
}
