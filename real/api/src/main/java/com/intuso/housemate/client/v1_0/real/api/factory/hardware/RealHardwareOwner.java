package com.intuso.housemate.client.v1_0.real.api.factory.hardware;

import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealHardwareOwner {
    public ChildOverview getAddHardwareCommandDetails();
    public void addHardware(RealHardware hardware);
    public void removeHardware(RealHardware hardware);
}
