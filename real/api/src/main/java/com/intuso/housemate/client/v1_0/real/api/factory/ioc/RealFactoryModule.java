package com.intuso.housemate.client.v1_0.real.api.factory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.client.v1_0.real.api.RealUser;
import com.intuso.housemate.client.v1_0.real.api.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.v1_0.real.api.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.v1_0.real.api.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.v1_0.real.api.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.api.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.v1_0.real.api.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.v1_0.real.api.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.v1_0.real.api.factory.task.AddTaskCommand;
import com.intuso.housemate.client.v1_0.real.api.factory.task.TaskFactoryType;
import com.intuso.housemate.client.v1_0.real.api.factory.user.AddUserCommand;

/**
 * Created by tomc on 20/03/15.
 */
public class RealFactoryModule extends AbstractModule {
    @Override
    protected void configure() {

        // automations
        install(new FactoryModuleBuilder().build(RealAutomation.Factory.class));
        install(new FactoryModuleBuilder().build(AddAutomationCommand.Factory.class));

        // conditions
        bind(ConditionFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddConditionCommand.Factory.class));

        // devices
        bind(DeviceFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddDeviceCommand.Factory.class));

        // hardwares
        bind(HardwareFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Factory.class));

        // tasks
        bind(TaskFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddTaskCommand.Factory.class));

        // users
        install(new FactoryModuleBuilder().build(RealUser.Factory.class));
        install(new FactoryModuleBuilder().build(AddUserCommand.Factory.class));
    }
}
