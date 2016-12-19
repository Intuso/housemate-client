package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.Command;
import com.intuso.housemate.client.v1_0.api.annotation.Feature;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Interface to mark real devices that provide volume control
 */
@Feature
@Id(value = "volume", name = "Volume", description = "Volume")
public interface VolumeControl {

    /**
     * Callback for when the volume should be muted
     */
    @Command
    @Id(value = "mute", name = "Mute", description = "Mute")
    void mute();

    /**
     * Callback for when the volume should be increased
     */
    @Command
    @Id(value = "volume-up", name = "Volume Up", description = "Volume up")
    void volumeUp();

    /**
     * Callback for when the volume should be decreased
     */
    @Command
    @Id(value = "volume-down", name = "Volume Down", description = "Volume down")
    void volumeDown();
}
