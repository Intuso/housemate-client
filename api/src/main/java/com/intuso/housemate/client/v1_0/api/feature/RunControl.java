package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.Command;
import com.intuso.housemate.client.v1_0.api.annotation.Feature;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Interface to mark real devices that provide power control
 */
@Feature
@Id(value = "run", name = "Run", description = "Run")
public interface RunControl {

    /**
     * Callback to turn the device on
     */
    @Command
    @Id(value = "start", name = "Start", description = "Start")
    void start();

    /**
     * Callback to turn the device off
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stop();
}
