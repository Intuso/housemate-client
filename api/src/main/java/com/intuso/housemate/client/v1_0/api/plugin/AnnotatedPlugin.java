package com.intuso.housemate.client.v1_0.api.plugin;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public abstract class AnnotatedPlugin implements Plugin {

    @Override
    public Id getId() {
        return getClass().getAnnotation(Id.class);
    }

    @Override
    public Iterable<ChoiceType> getChoiceTypes() {
        ChoiceTypes types = getClass().getAnnotation(ChoiceTypes.class);
        if(types == null)
            return new ArrayList<>();
        return Arrays.asList(types.value());
    }

    @Override
    public Iterable<Class<?>> getCompositeTypes() {
        CompositeTypes types = getClass().getAnnotation(CompositeTypes.class);
        if(types == null)
            return new ArrayList<>();
        return Arrays.asList(types.value());
    }

    @Override
    public Iterable<DoubleRangeType> getDoubleRangeTypes() {
        DoubleRangeTypes types = getClass().getAnnotation(DoubleRangeTypes.class);
        if(types == null)
            return new ArrayList<>();
        return Arrays.asList(types.value());
    }

    @Override
    public Iterable<IntegerRangeType> getIntegerRangeTypes() {
        IntegerRangeTypes types = getClass().getAnnotation(IntegerRangeTypes.class);
        if(types == null)
            return new ArrayList<>();
        return Arrays.asList(types.value());
    }

    @Override
    public Iterable<RegexType> getRegexTypes() {
        RegexTypes types = getClass().getAnnotation(RegexTypes.class);
        if(types == null)
            return new ArrayList<>();
        return Arrays.asList(types.value());
    }

    @Override
    public Iterable<Class<? extends ConditionDriver>> getConditionDrivers() {
        ConditionDrivers taskDrivers = getClass().getAnnotation(ConditionDrivers.class);
        if(taskDrivers == null)
            return new ArrayList<>();
        return Arrays.asList(taskDrivers.value());
    }

    @Override
    public Iterable<HardwareDriver> getHardwareDrivers() {
        HardwareDrivers taskDrivers = getClass().getAnnotation(HardwareDrivers.class);
        if(taskDrivers == null)
            return new ArrayList<>();
        return Arrays.asList(taskDrivers.value());
    }

    @Override
    public Iterable<Class<? extends TaskDriver>> getTaskDrivers() {
        TaskDrivers taskDrivers = getClass().getAnnotation(TaskDrivers.class);
        if(taskDrivers == null)
            return new ArrayList<>();
        return Arrays.asList(taskDrivers.value());
    }
}
