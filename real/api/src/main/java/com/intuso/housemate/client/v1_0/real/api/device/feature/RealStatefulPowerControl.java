package com.intuso.housemate.client.v1_0.real.api.device.feature;

import com.intuso.housemate.client.v1_0.real.api.annotations.FeatureId;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.object.v1_0.api.feature.StatefulPowerControl;

/**
 * Interface to mark real devices that provide stateful power control
 */
@FeatureId(StatefulPowerControl.ID)
public interface RealStatefulPowerControl extends RealPowerControl {
    interface Values {

        /**
         * Callback to set the current power status of the device
         * @param isOn true if the device is now on
         */
        @Value(id = "is-on", name = "Is On", description = "True if the device is currently on", typeId = "boolean")
        void isOn(boolean isOn);
    }
}
