package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealRootImpl;
import com.intuso.housemate.client.v1_0.real.impl.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.v1_0.real.impl.factory.ioc.RealFactoryModule;
import com.intuso.housemate.client.v1_0.real.impl.type.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class RealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealFactoryModule());
        install(new RealAnnotationsModule());

        bind(RealRoot.class).to(RealRootImpl.class);
        bind(RealRootImpl.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealRootImpl.class);
        bind(RealApplication.Container.class).to(RealRootImpl.class);
        bind(RealDevice.Container.class).to(RealRootImpl.class);
        bind(RealHardware.Container.class).to(RealRootImpl.class);
        bind(RealType.Container.class).to(RealRootImpl.class);
        bind(RealUser.Container.class).to(RealRootImpl.class);

        // bind everything as singletons that should be
        bind(ApplicationStatusType.class).in(Scopes.SINGLETON);
        bind(ApplicationInstanceStatusType.class).in(Scopes.SINGLETON);
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(EmailType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public RealList<RealType<?>> getTypes(Logger logger, ListenersFactory listenersFactory) {
        return (RealList)new RealListImpl<>(logger, listenersFactory, RealRootImpl.TYPES_ID, "Types", "Types");
    }
}
