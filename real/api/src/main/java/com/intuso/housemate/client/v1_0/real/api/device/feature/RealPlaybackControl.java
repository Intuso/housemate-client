package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.Command;
import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;

/**
 * Interface to mark real devices that provide playback control
 */
@FeatureId("playback")
public interface RealPlaybackControl extends RealFeature {

    /**
     * Callback to start playback
     */
    @Command(id = "play", name = "Play", description = "Play")
    void play();

    /**
     * Callback to pause playback
     */
    @Command(id = "pause", name = "Pause", description = "Pause")
    void pause();

    /**
     * Callback to stop playback
     */
    @Command(id = "stop", name = "Stop", description = "Stop")
    void stopPlayback();

    /**
     * Callback to skip the playback forwards
     */
    @Command(id = "forward", name = "Forward", description = "Forward")
    void forward();

    /**
     * Callback to skip the playback backwards
     */
    @Command(id = "rewind", name = "Rewind", description = "Rewind")
    void rewind();
}
