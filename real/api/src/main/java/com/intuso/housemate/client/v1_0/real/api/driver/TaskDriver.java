package com.intuso.housemate.client.v1_0.real.api.driver;

import org.slf4j.Logger;

/**
 * Created by tomc on 30/09/15.
 */
public interface TaskDriver {

    void execute();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends TaskDriver> {
        DRIVER create(Logger logger, Callback callback);
    }
}
