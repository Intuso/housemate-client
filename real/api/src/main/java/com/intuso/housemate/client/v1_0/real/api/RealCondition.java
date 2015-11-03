package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;
import com.intuso.housemate.object.v1_0.api.Condition;

public interface RealCondition<DRIVER extends ConditionDriver>
        extends Condition<RealCommand,
        RealValue<String>,
        RealProperty<PluginResource<ConditionDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealValue<Boolean>,
        RealList<? extends RealProperty<?>>, RealCommand, RealCondition<?>,
        RealList<? extends RealCondition<?>>,
        RealCondition<DRIVER>>,
        Condition.Listener<RealCondition<?>>,
        ConditionDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isSatisfied();

    /**
     * Sets the error message for the object
     * @param error
     */
    void setError(String error);

    /**
     * Updates the satisfied value of the condition. If different, it will propagate to the parent. If It affects the
     * parent's satisfied value then it will propagate again until either it does not affect a parent condition or it
     * gets to the automation, in which case the tasks for the new value will be executed
     * @param satisfied
     */
    void conditionSatisfied(boolean satisfied);

    void start();

    void stop();

    interface RemovedListener {
        void conditionRemoved(RealCondition condition);
    }

    interface Factory {
        RealCondition<?> create(ConditionData data, RemovedListener removedListener);
    }
}
