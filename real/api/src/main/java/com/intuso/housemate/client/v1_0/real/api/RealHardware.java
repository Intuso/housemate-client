package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.api.driver.PluginDependency;
import com.intuso.housemate.client.v1_0.api.object.Hardware;

/**
 * Base class for all hardwares
 */
public interface RealHardware<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginDependency<HardwareDriver.Factory<?>>, ?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        DEVICES extends RealList<? extends RealDevice<?, ?, ?, ?, ?>, ?>,
        HARDWARE extends RealHardware<COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, COMMANDS, VALUES, PROPERTIES, DEVICES, HARDWARE>>
        extends Hardware<COMMAND,
        COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        DRIVER_PROPERTY,
        BOOLEAN_VALUE,
        COMMANDS,
        VALUES,
        PROPERTIES,
        DEVICES,
        HARDWARE>,
        HardwareDriver.Callback {

    <DRIVER extends HardwareDriver> DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isRunning();

    interface Container<HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, HARDWARES extends RealList<? extends HARDWARE, ?>> extends Hardware.Container<HARDWARES>, RemoveCallback<HARDWARE> {
        void addHardware(HARDWARE hardware);
    }

    interface RemoveCallback<HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?, ?, ?>> {
        void removeHardware(HARDWARE hardware);
    }
}
