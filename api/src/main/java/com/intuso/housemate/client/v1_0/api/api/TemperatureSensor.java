package com.intuso.housemate.client.v1_0.api.api;

import com.intuso.housemate.client.v1_0.api.annotation.AddListener;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for temperature monitoring
 */
@Id(value = "temperature", name = "Temperature", description = "Temperature")
public interface TemperatureSensor {

    String ID = TemperatureSensor.class.getAnnotation(Id.class).value();

    /**
     * Add a listener
     */
    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {

        /**
         * Callback for when the temperature of the device has changed
         * @param temperature the new temperature
         */
        @Value
        @Id(value = "temperature", name = "Temperature", description = "The current temperature, or null if unknown")
        void temperature(Double temperature);
    }
}
