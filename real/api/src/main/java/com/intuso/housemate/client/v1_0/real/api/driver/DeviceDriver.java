package com.intuso.housemate.client.v1_0.real.api.driver;

import org.slf4j.Logger;

/**
 * Created by tomc on 30/09/15.
 */
public interface DeviceDriver {

    void start();
    void stop();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends DeviceDriver> {
        DRIVER create(Logger logger, Callback callback);
    }
}
