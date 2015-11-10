package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;

/**
 * Interface to mark real devices that provide stateful playback control
 */
@FeatureId("playback-stateful")
public interface RealStatefulPlaybackControl extends RealPlaybackControl {
    public interface Values {

        /**
         * Callback to set the current playing value
         * @param isPlaying true if the device is currently playing
         */
        @Value(id = "is-playing", name = "Is Playing", description = "True if the device is currently playing", typeId = "boolean")
        void isPlaying(boolean isPlaying);
    }
}
