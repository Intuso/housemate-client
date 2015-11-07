package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver;
import com.intuso.housemate.comms.v1_0.api.payload.TaskData;
import com.intuso.housemate.object.v1_0.api.Task;

public interface RealTask<DRIVER extends TaskDriver>
        extends Task<
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealList<RealProperty<?>>, RealTask<DRIVER>>,TaskDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isExecuting();

    /**
     * Executes this task
     */
    void executeTask();

    interface Container extends Task.Container<RealList<RealTask<?>>>, RemoveCallback {
        <DRIVER extends TaskDriver> RealTask<DRIVER> createAndAddTask(TaskData data);
        void addTask(RealTask<?> task);
    }

    interface RemoveCallback {
        void removeTask(RealTask<?> task);
    }

    interface Factory {
        RealTask<?> create(TaskData data, RemoveCallback removeCallback);
    }
}
