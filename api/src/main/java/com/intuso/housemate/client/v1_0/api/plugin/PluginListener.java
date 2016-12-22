package com.intuso.housemate.client.v1_0.api.plugin;

import com.google.inject.Injector;

/**
 */
public interface PluginListener {
    void pluginAdded(Injector pluginInjector);
    void pluginRemoved(Injector pluginInjector);
}
