package com.intuso.housemate.client.v1_0.api.feature;

import com.intuso.housemate.client.v1_0.api.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for running something
 */
@Id(value = "run", name = "Run", description = "Run")
public interface RunControl {

    String ID = RunControl.class.getAnnotation(Id.class).value();

    /**
     * Start
     */
    @Command
    @Id(value = "start", name = "Start", description = "Start")
    void start();

    /**
     * Stop
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stop();

    /**
     * Add a listener
     */
    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {

        /**
         * Callback when running starts or stops
         * @param running true if the device is now running
         */
        @Value
        @Id(value = "running", name = "Running", description = "True if the device is currently running, null if unknown")
        void running(Boolean running);
    }
}
