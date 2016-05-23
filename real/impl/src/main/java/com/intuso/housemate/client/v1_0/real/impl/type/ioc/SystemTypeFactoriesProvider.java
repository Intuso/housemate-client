package com.intuso.housemate.client.v1_0.real.impl.type.ioc;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.v1_0.real.impl.type.TypeFactories;
import com.intuso.housemate.plugin.v1_0.api.driver.*;
import com.intuso.housemate.plugin.v1_0.api.type.Email;

/**
 * Created by tomc on 23/05/16.
 */
public class SystemTypeFactoriesProvider implements Provider<Iterable<TypeFactories<?>>> {

    // standard java types
    private final TypeFactories<Boolean> booleanTypeFactories;
    private final TypeFactories<Double> doubleTypeFactories;
    private final TypeFactories<Integer> integerTypeFactories;
    private final TypeFactories<String> stringTypeFactories;

    // other common types defined by us
    private final TypeFactories<Email> emailTypeFactories;

    // driver factory types
    private final TypeFactories<PluginResource<ConditionDriver.Factory<?>>> conditionDriverTypeFactories;
    private final TypeFactories<PluginResource<DeviceDriver.Factory<?>>> deviceDriverTypeFactories;
    private final TypeFactories<PluginResource<HardwareDriver.Factory<?>>> hardwareDriverTypeFactories;
    private final TypeFactories<PluginResource<TaskDriver.Factory<?>>> taskDriverTypeFactories;

    @Inject
    public SystemTypeFactoriesProvider(TypeFactories<Boolean> booleanTypeFactories,
                                       TypeFactories<Double> doubleTypeFactories,
                                       TypeFactories<Integer> integerTypeFactories,
                                       TypeFactories<String> stringTypeFactories,
                                       TypeFactories<Email> emailTypeFactories,
                                       TypeFactories<PluginResource<ConditionDriver.Factory<? extends ConditionDriver>>> conditionDriverTypeFactories,
                                       TypeFactories<PluginResource<DeviceDriver.Factory<? extends DeviceDriver>>> deviceDriverTypeFactories,
                                       TypeFactories<PluginResource<HardwareDriver.Factory<? extends HardwareDriver>>> hardwareDriverTypeFactories,
                                       TypeFactories<PluginResource<TaskDriver.Factory<? extends TaskDriver>>> taskDriverTypeFactories) {
        this.booleanTypeFactories = booleanTypeFactories;
        this.doubleTypeFactories = doubleTypeFactories;
        this.integerTypeFactories = integerTypeFactories;
        this.stringTypeFactories = stringTypeFactories;
        this.emailTypeFactories = emailTypeFactories;
        this.conditionDriverTypeFactories = conditionDriverTypeFactories;
        this.deviceDriverTypeFactories = deviceDriverTypeFactories;
        this.hardwareDriverTypeFactories = hardwareDriverTypeFactories;
        this.taskDriverTypeFactories = taskDriverTypeFactories;
    }

    @Override
    public Iterable<TypeFactories<?>> get() {
        return Lists.newArrayList(
                booleanTypeFactories,
                doubleTypeFactories,
                integerTypeFactories,
                stringTypeFactories,

                emailTypeFactories,

                conditionDriverTypeFactories,
                deviceDriverTypeFactories,
                hardwareDriverTypeFactories,
                taskDriverTypeFactories
        );
    }
}