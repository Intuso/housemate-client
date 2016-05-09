package com.intuso.housemate.client.v1_0.real.impl.factory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.impl.*;
import com.intuso.housemate.client.v1_0.real.impl.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.factory.task.AddTaskCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.task.TaskFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.factory.user.AddUserCommand;

/**
 * Created by tomc on 20/03/15.
 */
public class RealFactoryModule extends AbstractModule {
    @Override
    protected void configure() {

        // automations
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealAutomationImpl.Factory>() {}));
        install(new FactoryModuleBuilder().build(AddAutomationCommand.Factory.class));

        // condition
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealConditionImpl.Factory>() {}));
        bind(ConditionFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddConditionCommand.Factory.class));

        // devices
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealDeviceImpl.Factory>() {}));
        bind(DeviceFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddDeviceCommand.Factory.class));

        // features
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealFeatureImpl.Factory>() {}));

        // hardwares
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealHardwareImpl.Factory>() {}));
        bind(HardwareFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Factory.class));

        // parameters
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealParameterImpl.Factory>() {}));

        // tasks
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealTaskImpl.Factory>() {}));
        bind(TaskFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddTaskCommand.Factory.class));

        // users
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealUserImpl.Factory>() {}));
        install(new FactoryModuleBuilder().build(AddUserCommand.Factory.class));

        // values
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealValueImpl.Factory>() {}));
    }
}
