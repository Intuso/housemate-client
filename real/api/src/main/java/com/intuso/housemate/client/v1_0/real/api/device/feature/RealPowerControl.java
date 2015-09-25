package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.Command;
import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;
import com.intuso.housemate.object.v1_0.api.feature.PowerControl;

/**
 * Interface to mark real devices that provide power control
 */
@FeatureId(PowerControl.ID)
public interface RealPowerControl extends RealFeature {

    /**
     * Callback to turn the device on
     */
    @Command(id = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Callback to turn the device off
     */
    @Command(id = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();
}
