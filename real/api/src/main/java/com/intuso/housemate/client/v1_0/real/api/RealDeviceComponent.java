package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.DeviceComponent;

/**
 * Base class for all hardwares
 */
public interface RealDeviceComponent<
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE_COMPONENT extends RealDeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT>>
        extends DeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT> {}
