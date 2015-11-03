package com.intuso.housemate.client.v1_0.real.impl.factory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.api.*;
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
                .implement(RealAutomation.class, RealAutomationImpl.class)
                .build(RealAutomation.Factory.class));
        install(new FactoryModuleBuilder().build(AddAutomationCommand.Factory.class));

        // conditions
        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<RealCondition<?>>() {}, new TypeLiteral<RealConditionImpl<?>>() {})
                .build(RealCondition.Factory.class));
        bind(ConditionFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddConditionCommand.Factory.class));

        // devices
        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<RealDevice<?>>() {}, new TypeLiteral<RealDeviceImpl<?>>() {})
                .build(RealDevice.Factory.class));
        bind(DeviceFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddDeviceCommand.Factory.class));

        // hardwares
        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<RealHardware<?>>() {}, new TypeLiteral<RealHardwareImpl<?>>() {})
                .build(RealHardware.Factory.class));
        bind(HardwareFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Factory.class));

        // tasks
        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<RealTask<?>>() {}, new TypeLiteral<RealTaskImpl<?>>() {})
                .build(RealTask.Factory.class));
        bind(TaskFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddTaskCommand.Factory.class));

        // users
        install(new FactoryModuleBuilder()
                .implement(RealUser.class, RealUserImpl.class)
                .build(RealUser.Factory.class));
        install(new FactoryModuleBuilder().build(AddUserCommand.Factory.class));
    }
}
