package com.intuso.housemate.client.v1_0.api.ability;

import com.intuso.housemate.client.v1_0.api.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling power
 */
@Id(value = "power", name = "Power", description = "Power")
public interface Power extends Ability {

    String ID = Power.class.getAnnotation(Id.class).value();

    /**
     * Turn on
     */
    @Command
    @Id(value = "on", name = "Turn On", description = "Turn on")
    void turnOn();

    /**
     * Turn off
     */
    @Command
    @Id(value = "off", name = "Turn Off", description = "Turn off")
    void turnOff();

    /**
     * Add a listener
     */
    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {

        /**
         * Callback for when the device has been turned on or off
         * @param on true if the device is now on, null if unknown
         */
        @Value
        @Id(value = "on", name = "On", description = "Whether the device is on")
        void on(Boolean on);
    }

    @Id(value = "power.variable", name = "Variable power", description = "Variable power")
    interface Variable extends Power {

        /**
         * Set the power
         */
        @Command
        @Id(value = "set", name = "Set", description = "Set Power")
        void percent(@Id(value = "percent", name = "Percent", description = "Percent") int volume);

        /**
         * Increase power
         */
        @Command
        @Id(value = "increase", name = "Increase", description = "Increase power")
        void increase();

        /**
         * Decrease power
         */
        @Command
        @Id(value = "decrease", name = "Decrease", description = "Decrease power")
        void decrease();

        interface Listener {

            /**
             * Callback for when the device power has been changed
             * @param percent the percent of power to the device, null if unknown
             */
            @Value
            @Id(value = "percent", name = "Percent", description = "Percent")
            void percent(Integer percent);
        }
    }
}
