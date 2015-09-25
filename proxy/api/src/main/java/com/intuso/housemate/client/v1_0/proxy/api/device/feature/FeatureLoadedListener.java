package com.intuso.housemate.client.v1_0.proxy.api.device.feature;

import com.intuso.housemate.object.v1_0.api.Device;

/**
 * Listener interface for when a feature has finished loading
 * @param <DEVICE> the device type
 * @param <FEATURE> the feature type
 */
public interface FeatureLoadedListener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, FEATURE extends Device.Feature> {

    public void loadFailed(DEVICE device, FEATURE feature);

    /**
     * Callback method for when the feature has finished loading
     * @param device the device the feature was loaded for
     * @param feature the feature that was loaded
     */
    public void loadFinished(DEVICE device, FEATURE feature);
}
