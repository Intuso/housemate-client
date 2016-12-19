package com.intuso.housemate.client.v1_0.api.plugin;

import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Created by tomc on 06/11/15.
 */
public class PluginResourceImpl<RESOURCE> implements PluginResource<RESOURCE> {

    private final Id id;
    private final RESOURCE resource;

    public PluginResourceImpl(Id id, RESOURCE resource) {
        this.id = id;
        this.resource = resource;
    }

    public Id getId() {
        return id;
    }

    @Override
    public RESOURCE getResource() {
        return resource;
    }
}
