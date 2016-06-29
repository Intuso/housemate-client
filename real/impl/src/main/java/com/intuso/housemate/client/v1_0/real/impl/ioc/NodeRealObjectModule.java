package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealNodeImpl;
import com.intuso.housemate.client.v1_0.real.impl.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.v1_0.real.impl.type.ioc.RealTypesModule;
import com.intuso.housemate.client.v1_0.real.impl.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class NodeRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealAnnotationsModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealNode.class).to(RealNodeImpl.class);
        bind(RealNodeImpl.class).in(Scopes.SINGLETON);

        bind(RealHardware.Container.class).to(RealNodeImpl.class);
    }

    @Provides
    @Node
    public Logger getRootLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Node Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
