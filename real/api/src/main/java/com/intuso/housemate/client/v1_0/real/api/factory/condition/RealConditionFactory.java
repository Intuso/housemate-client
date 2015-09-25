package com.intuso.housemate.client.v1_0.real.api.factory.condition;

import com.intuso.housemate.client.v1_0.real.api.RealCondition;
import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealConditionFactory<CONDITION extends RealCondition> {
    public CONDITION create(ConditionData data, RealConditionOwner owner);
}
