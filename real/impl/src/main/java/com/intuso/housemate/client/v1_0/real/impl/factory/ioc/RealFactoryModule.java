package com.intuso.housemate.client.v1_0.real.impl.factory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.client.v1_0.real.impl.RealHardwareImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.HardwareFactoryType;

/**
 * Created by tomc on 20/03/15.
 */
public class RealFactoryModule extends AbstractModule {
    @Override
    protected void configure() {

        // hardwares
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealHardware.Factory<RealHardwareImpl<?>>>() {}));
        bind(HardwareFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Factory.class));

        // parameters
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealParameter.Factory<RealParameterImpl<?>>>() {}));

        // values
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealValue.Factory<RealValueImpl<?>>>() {}));
    }
}
