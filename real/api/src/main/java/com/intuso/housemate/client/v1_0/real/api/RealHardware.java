package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.object.v1_0.api.Hardware;
import org.slf4j.Logger;

/**
 * Base class for all hardwares
 */
public interface RealHardware<DRIVER extends HardwareDriver>
        extends Hardware<
        RealCommand,
        RealCommand,
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealList<RealProperty<?>>,
        RealHardware<DRIVER>>,HardwareDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isRunning();

    interface Container extends Hardware.Container<RealList<RealHardware<?>>>, RemoveCallback {
        <DRIVER extends HardwareDriver> RealHardware<DRIVER> createAndAddHardware(HardwareData data);
        void addHardware(RealHardware<?> hardware);
    }

    interface RemoveCallback {
        void removeHardware(RealHardware<?> hardware);
    }

    interface Factory {
        RealHardware<?> create(Logger logger, HardwareData data, RemoveCallback removeCallback);
    }
}
