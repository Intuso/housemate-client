package com.intuso.housemate.client.v1_0.real.api.module;

import com.intuso.housemate.client.v1_0.real.api.annotations.Id;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    Id getId();
    RESOURCE getResource();
}
