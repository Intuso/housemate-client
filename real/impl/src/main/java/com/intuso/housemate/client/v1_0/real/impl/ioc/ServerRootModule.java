package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealServerRoot;
import com.intuso.housemate.client.v1_0.real.impl.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.v1_0.real.impl.type.ioc.RealTypesModule;
import com.intuso.housemate.client.v1_0.real.impl.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ServerRootModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealAnnotationsModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealServer.class).to(RealServerRoot.class);
        bind(RealServerRoot.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealServerRoot.class);
        bind(RealDevice.Container.class).to(RealServerRoot.class);
        bind(RealUser.Container.class).to(RealServerRoot.class);
        bind(RealNode.Container.class).to(RealServerRoot.class);

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealServerRoot.Service.class);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }

    @Provides
    @Node
    public Logger getNodeLogger(@Server Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "nodes", "local");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Node Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
