package com.intuso.housemate.client.v1_0.real.impl.driver;

import com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver;

import java.util.Map;

public abstract class LogicCondition implements ConditionDriver {

    private final ConditionDriver.Callback conditionCallback;

    public LogicCondition(ConditionDriver.Callback conditionCallback) {
        this.conditionCallback = conditionCallback;
    }

    @Override
    public boolean hasChildConditions() {
        return true;
    }

    @Override
    public final void start() {
        conditionCallback.conditionSatisfied(checkIfSatisfied(conditionCallback.getChildSatisfied()));
    }

    @Override
    public final void stop() {
        // do nothing
    }

    /**
     * Checks if this condition is satisfied, given the satisfied state of the children
     * @param satisfiedMap the map of children to their satisfied value
     * @return true if this condition is currently satisfied
     */
    protected abstract boolean checkIfSatisfied(Map<String, Boolean> satisfiedMap);
}
