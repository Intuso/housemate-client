package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        FEATURE extends RealFeature<?, ?, ?, ?, ?, ?, ?, ?>,
        FEATURES extends RealList<? extends FEATURE, ?>,
        DEVICE extends RealDevice<COMMAND, BOOLEAN_VALUE, STRING_VALUE, FEATURE, FEATURES, DEVICE>>
        extends Device<
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        FEATURES,
        DEVICE>,
        RealFeature.Container<FEATURE, FEATURES> {

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES>, RemoveCallback<DEVICE> {
        void addDevice(DEVICE device);
    }

    interface RemoveCallback<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?>> {
        void removeDevice(DEVICE device);
    }
}
