package com.intuso.housemate.client.v1_0.real.api.factory.device;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealDeviceFactory<DEVICE extends RealDevice> {
    public DEVICE create(DeviceData data, RealDeviceOwner owner);
}
