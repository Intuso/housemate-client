package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.RealNodeImpl;
import com.intuso.housemate.client.v1_0.real.impl.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.v1_0.real.impl.factory.ioc.RealFactoryModule;

/**
 */
public class NodeRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealFactoryModule());
        install(new RealAnnotationsModule());
        install(new RealTypesModule());

        bind(RealNode.class).to(RealNodeImpl.class);
        bind(RealNodeImpl.class).in(Scopes.SINGLETON);

        bind(RealHardware.Container.class).to(RealNodeImpl.class);
    }
}
