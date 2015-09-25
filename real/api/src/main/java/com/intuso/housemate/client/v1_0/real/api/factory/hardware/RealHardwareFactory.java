package com.intuso.housemate.client.v1_0.real.api.factory.hardware;

import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealHardwareFactory<HARDWARE extends RealHardware> {
    public HARDWARE create(HardwareData data, RealHardwareOwner owner);
}
