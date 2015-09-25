package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.object.v1_0.api.feature.StatefulVolumeControl;

/**
 * Interface to mark real devices that provide stateful volume control
 */
@FeatureId(StatefulVolumeControl.ID)
public interface RealStatefulVolumeControl extends RealVolumeControl {
    public interface Values {

        /**
         * Callback for when the current volume has changed
         * @param currentVolume the new current volume
         */
        @Value(id = "current-volume", name = "Current Volume", description = "The device's current volume", typeId = "integer")
        void currentVolume(int currentVolume);
    }
}
