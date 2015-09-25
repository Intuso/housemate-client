package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.Command;
import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;
import com.intuso.housemate.object.v1_0.api.feature.VolumeControl;

/**
 * Interface to mark real devices that provide volume control
 */
@FeatureId(VolumeControl.ID)
public interface RealVolumeControl extends RealFeature {

    /**
     * Callback for when the volume should be muted
     */
    @Command(id = "mute", name = "Mute", description = "Mute")
    public void mute();

    /**
     * Callback for when the volume should be increased
     */
    @Command(id = "volume-up", name = "Volume Up", description = "Volume up")
    public void volumeUp();

    /**
     * Callback for when the volume should be decreased
     */
    @Command(id = "volume-down", name = "Volume Down", description = "Volume down")
    public void volumeDown();
}
