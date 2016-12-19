package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.Command;
import com.intuso.housemate.client.v1_0.api.annotation.Feature;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Interface to mark real devices that provide power control
 */
@Feature
@Id(value = "power", name = "Power", description = "Power")
public interface PowerControl {

    /**
     * Callback to turn the device on
     */
    @Command
    @Id(value = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Callback to turn the device off
     */
    @Command
    @Id(value = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();
}
