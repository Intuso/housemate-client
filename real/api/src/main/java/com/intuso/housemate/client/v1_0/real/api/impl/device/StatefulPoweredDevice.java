package com.intuso.housemate.client.v1_0.real.api.impl.device;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealStatefulPowerControl;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class StatefulPoweredDevice
        extends RealDevice
        implements RealStatefulPowerControl {

    @com.intuso.housemate.client.v1_0.real.api.annotations.Values
    public Values values;

	public StatefulPoweredDevice(Log log, ListenersFactory listenersFactory, String type, DeviceData data) {
		super(log, listenersFactory, type, data);
	}

    /**
     * Sets the device to be on
     */
    public final void setOn() {
        values.isOn(true);
    }

    /**
     * Sets the device to be off
     */
    public final void setOff() {
        values.isOn(false);
    }
}
