package com.intuso.housemate.client.v1_0.real.api.factory.automation;

import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.comms.v1_0.api.payload.AutomationData;

/**
* Created by tomc on 20/03/15.
*/
public interface RealAutomationFactory {
    public RealAutomation create(AutomationData data, RealAutomationOwner owner);
}
