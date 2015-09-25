package com.intuso.housemate.client.v1_0.real.api.factory.task;

import com.intuso.housemate.client.v1_0.real.api.RealTask;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealTaskOwner {
    ChildOverview getAddTaskCommandDetails();
    void addTask(RealTask task);
    void removeTask(RealTask task);
}
