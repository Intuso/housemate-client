package com.intuso.housemate.client.v1_0.real.api.factory.task;

import com.intuso.housemate.client.v1_0.real.api.RealTask;
import com.intuso.housemate.comms.v1_0.api.payload.TaskData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealTaskFactory<TASK extends RealTask> {
    public TASK create(TaskData data, RealTaskOwner owner);
}
