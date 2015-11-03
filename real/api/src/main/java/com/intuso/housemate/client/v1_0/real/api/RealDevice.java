package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.object.v1_0.api.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<DRIVER extends DeviceDriver>
        extends Device<
        RealCommand,
        RealCommand,
        RealCommand,
        RealCommand,
        RealList<? extends RealCommand>,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealValue<?>,
        RealList<? extends RealValue<?>>,
        RealProperty<?>,
        RealList<? extends RealProperty<?>>,
        RealDevice<DRIVER>>, DeviceDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    interface RemovedListener {
        void deviceRemoved(RealDevice device);
    }

    interface Factory {
        RealDevice<?> create(DeviceData data, RemovedListener removedListener);
    }
}
