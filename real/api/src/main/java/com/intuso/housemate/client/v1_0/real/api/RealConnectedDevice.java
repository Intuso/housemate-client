package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.ConnectedDevice;

/**
 * Base class for all devices
 */
public interface RealConnectedDevice<COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        DEVICE extends RealConnectedDevice<COMMAND, COMMANDS, VALUES, PROPERTIES, DEVICE>>
        extends ConnectedDevice<COMMAND,
                COMMANDS,
                VALUES,
                PROPERTIES,
                DEVICE> {

    interface Container<DEVICE extends RealConnectedDevice<?, ?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends ConnectedDevice.Container<DEVICES> {
        void addDevice(DEVICE device);
    }
}
