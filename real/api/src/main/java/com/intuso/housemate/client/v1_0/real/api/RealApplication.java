package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.Application;

public interface RealApplication
        extends Application<
            RealValue<Application.Status>,
            RealCommand,
            RealApplicationInstance,
            RealList<? extends RealApplicationInstance>,
            RealApplication> {

    void setStatus(Application.Status status);
}
