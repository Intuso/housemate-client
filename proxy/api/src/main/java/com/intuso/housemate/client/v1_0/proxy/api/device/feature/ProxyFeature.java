package com.intuso.housemate.client.v1_0.proxy.api.device.feature;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.object.v1_0.api.Device;

import java.util.Set;

/**
 * Base interface for all proxy features
 * @param <FEATURE> the feature type
 * @param <DEVICE> the device type
 */
public interface ProxyFeature<
            FEATURE extends ProxyFeature<?, ?>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?>>
        extends Device.Feature {

    /**
     * Get the ids of the commands that the feature requires
     * @return the ids of the commands that the feature requires
     */
    public Set<String> getCommandIds();

    /**
     * Get the ids of the values that the feature requires
     * @return the ids of the values that the feature requires
     */
    public Set<String> getValueIds();

    /**
     * Get the ids of the properties that the feature requires
     * @return the ids of the properties that the feature requires
     */
    public Set<String> getPropertyIds();

    /**
     * Load everything the feature needs
     * @param listener a listener for when the load is finished
     */
    public void load(FeatureLoadedListener<DEVICE, FEATURE> listener);

    /**
     * Get the feature as it's proper type
     * @return the feature as its proper type
     */
    public FEATURE getThis();
}
