package com.intuso.housemate.client.v1_0.api.plugin;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;

/**
 * Created by tomc on 16/01/17.
 */
public interface Plugin {
    Id getId();
    Iterable<ChoiceType> getChoiceTypes();
    Iterable<Class<?>> getCompositeTypes();
    Iterable<DoubleRangeType> getDoubleRangeTypes();
    Iterable<IntegerRangeType> getIntegerRangeTypes();
    Iterable<RegexType> getRegexTypes();
    Iterable<Class<? extends ConditionDriver>> getConditionDrivers();
    Iterable<HardwareDriver> getHardwareDrivers();
    Iterable<Class<? extends TaskDriver>> getTaskDrivers();
}

