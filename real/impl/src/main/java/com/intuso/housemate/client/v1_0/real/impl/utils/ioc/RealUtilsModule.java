package com.intuso.housemate.client.v1_0.real.impl.utils.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.real.impl.utils.*;

/**
 * Created by tomc on 19/05/16.
 */
public class RealUtilsModule extends AbstractModule {
    @Override
    protected void configure() {

        // add hardware command
        bind(AddHardwareCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Performer.Factory.class));
    }
}
