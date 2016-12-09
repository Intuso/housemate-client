package com.intuso.housemate.client.v1_0.real.api.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.Command;
import com.intuso.housemate.client.v1_0.real.api.annotations.Feature;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;

/**
 * Interface to mark real devices that provide power control
 */
@Feature
@TypeInfo(id = "power", name = "Power", description = "Power")
public interface PowerControl {

    /**
     * Callback to turn the device on
     */
    @Command
    @TypeInfo(id = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Callback to turn the device off
     */
    @Command
    @TypeInfo(id = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();
}
