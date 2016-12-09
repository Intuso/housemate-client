package com.intuso.housemate.client.v1_0.real.api.module;

import com.google.inject.Injector;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface PluginListener extends Listener {
    void pluginAdded(Injector pluginInjector);
    void pluginRemoved(Injector pluginInjector);
}
