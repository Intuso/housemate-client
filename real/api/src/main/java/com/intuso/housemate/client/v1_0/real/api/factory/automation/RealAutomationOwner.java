package com.intuso.housemate.client.v1_0.real.api.factory.automation;

import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealAutomationOwner {
    public ChildOverview getAddAutomationCommandDetails();
    public void addAutomation(RealAutomation automation);
    public void removeAutomation(RealAutomation automation);
}
