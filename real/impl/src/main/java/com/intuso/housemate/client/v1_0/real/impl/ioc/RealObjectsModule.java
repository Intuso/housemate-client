package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Hardware;
import com.intuso.housemate.client.v1_0.real.impl.*;

/**
 * Created by tomc on 20/03/15.
 */
public class RealObjectsModule extends AbstractModule {
    @Override
    protected void configure() {

        // commands
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealCommandImpl.Factory>() {}));

        // device components
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealDeviceComponentImpl.Factory>() {}));

        // device connecteds
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealDeviceConnectedImpl.Factory>() {}));

        // lists
        // generated
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealCommandImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealDeviceComponentImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealOptionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealParameterImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealPropertyImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealSubTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealValueImpl<?>>>() {}));
        // persisted
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Device.Connected.Data, RealDeviceConnectedImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Hardware.Data, RealHardwareImpl>>() {}));
        // persisted data classes
        bind(new TypeLiteral<Class<Device.Connected.Data>>() {}).toInstance(Device.Connected.Data.class);
        bind(new TypeLiteral<Class<Hardware.Data>>() {}).toInstance(Hardware.Data.class);
        // persisted element factories
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Device.Connected.Data, RealDeviceConnectedImpl>>() {}).to(RealDeviceConnectedImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Hardware.Data, RealHardwareImpl>>() {}).to(RealHardwareImpl.LoadPersisted.class);

        // hardwares
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealHardwareImpl.Factory>() {}));

        // options
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealOptionImpl.Factory>() {}));

        // parameter
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealParameterImpl.Factory>() {}));

        // property
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealPropertyImpl.Factory>() {}));

        // subtype
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealSubTypeImpl.Factory>() {}));

        // value
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealValueImpl.Factory>() {}));
    }
}
