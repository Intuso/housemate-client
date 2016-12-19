package com.intuso.housemate.client.v1_0.api.plugin;

import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    Id getId();
    RESOURCE getResource();
}
