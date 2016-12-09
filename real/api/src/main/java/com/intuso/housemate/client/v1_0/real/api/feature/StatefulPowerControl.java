package com.intuso.housemate.client.v1_0.real.api.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.Feature;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@TypeInfo(id = "power-stateful", name = "Power", description = "Power")
public interface StatefulPowerControl extends PowerControl {

    @Values
    interface PowerValues {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value("boolean")
        @TypeInfo(id = "is-on", name = "Is On", description = "True if the device is currently on")
        void isOn(boolean isOn);
    }
}
