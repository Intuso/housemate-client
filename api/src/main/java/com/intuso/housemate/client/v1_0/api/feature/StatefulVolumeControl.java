package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.Feature;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Value;
import com.intuso.housemate.client.v1_0.api.annotation.Values;

/**
 * Interface to mark real devices that provide stateful volume control
 */
@Feature
@Id(value = "volume-stateful", name = "Volume", description = "Volume")
public interface StatefulVolumeControl extends VolumeControl {

    @Values
    interface VolumeValues {

        /**
         * Callback for when the current volume has changed
         * @param currentVolume the new current volume
         */
        @Value("integer")
        @Id(value = "current-volume", name = "Current Volume", description = "The device's current volume")
        void currentVolume(int currentVolume);
    }
}
