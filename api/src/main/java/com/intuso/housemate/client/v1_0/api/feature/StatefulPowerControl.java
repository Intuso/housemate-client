package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.Feature;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Value;
import com.intuso.housemate.client.v1_0.api.annotation.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@Id(value = "power-stateful", name = "Power", description = "Power")
public interface StatefulPowerControl extends PowerControl {

    @Values
    interface PowerValues {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value("boolean")
        @Id(value = "is-on", name = "Is On", description = "True if the device is currently on")
        void isOn(boolean isOn);
    }
}
