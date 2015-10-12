package com.intuso.housemate.client.v1_0.real.api.driver;

/**
 * Created by tomc on 30/09/15.
 */
public interface HardwareDriver {

    void start();
    void stop();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends HardwareDriver> {
        DRIVER create(Callback callback);
    }
}
