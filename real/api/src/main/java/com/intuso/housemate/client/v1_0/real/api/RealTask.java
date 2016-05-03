package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Task;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver;
import org.slf4j.Logger;

public interface RealTask<DRIVER extends TaskDriver,
        COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>>,
        TASK extends RealTask<DRIVER, COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, TASK>>
        extends Task<COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        DRIVER_PROPERTY,
        BOOLEAN_VALUE,
        PROPERTIES,
        TASK>,
        TaskDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isExecuting();

    /**
     * Executes this task
     */
    void executeTask();

    interface Container<TASK extends RealTask<?, ?, ?, ?, ?, ?, ?>, TASKS extends RealList<? extends TASK>> extends Task.Container<TASKS>, RemoveCallback<TASK> {
        void addTask(TASK task);
    }

    interface RemoveCallback<TASK extends RealTask<?, ?, ?, ?, ?, ?, ?>> {
        void removeTask(TASK task);
    }

    interface Factory<TASK extends RealTask<?, ?, ?, ?, ?, ?, ?>> {
        TASK create(Logger logger, Data data, RemoveCallback removeCallback);
    }
}
