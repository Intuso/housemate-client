package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Base class for all devices
 */
public interface RealDeviceConnected<
        COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE extends RealDeviceConnected<COMMAND, COMMANDS, VALUES, DEVICE>>
        extends RealDevice<Device.Connected.Data, Device.Connected.Listener<? super DEVICE>, COMMAND, COMMANDS, VALUES, DEVICE>,
        Device.Connected<COMMAND, COMMANDS, VALUES, DEVICE> {

    interface Container<DEVICE extends RealDeviceConnected<?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Connected.Container<DEVICES> {
        void addConnectedDevice(DEVICE device);
    }
}
