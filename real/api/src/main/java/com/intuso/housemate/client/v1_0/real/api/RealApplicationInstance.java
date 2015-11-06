package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.ApplicationInstance;

public interface RealApplicationInstance
        extends ApplicationInstance<RealValue<ApplicationInstance.Status>, RealCommand, RealApplicationInstance> {

    interface Container extends ApplicationInstance.Container<RealList<RealApplicationInstance>> {
        void addApplicationInstance(RealApplicationInstance applicationInstance);
        void removeApplicationInstance(RealApplicationInstance applicationInstance);
    }
}
