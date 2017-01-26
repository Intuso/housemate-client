package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.*;
import com.intuso.utilities.listener.MemberRegistration;

/**
 * API for controlling power
 */
@Feature
@Id(value = "power", name = "Power", description = "Power")
public interface PowerControl {

    String ID = PowerControl.class.getAnnotation(Id.class).value();

    /**
     * Turn on
     */
    @Command
    @Id(value = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Turn off
     */
    @Command
    @Id(value = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();

    /**
     * API for controlling power with state
     */
    @Feature
    @Id(value = "power-stateful", name = "Power", description = "Power")
    interface Stateful extends PowerControl {

        String ID = Stateful.class.getAnnotation(Id.class).value();

        /**
         * Get whether the device is current on
         * @return true if the device is currently on
         */
        @Value
        @Id(value = "on", name = "On", description = "True if the device is currently on")
        boolean isOn();

        /**
         * Add a listener
         */
        @AddListener
        MemberRegistration addListener(Listener listener);
    }

    interface Listener {

        /**
         * Callback for when the device has been turned on or off
         * @param on true if the device is now on
         */
        @Value
        @Id(value = "on", name = "On", description = "True if the device is now on")
        void on(boolean on);
    }
}
