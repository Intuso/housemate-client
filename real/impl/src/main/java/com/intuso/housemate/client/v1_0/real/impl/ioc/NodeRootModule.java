package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealNodeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealObject;
import com.intuso.housemate.client.v1_0.real.impl.annotation.ioc.AnnotationParserV1_0Module;
import com.intuso.housemate.client.v1_0.real.impl.type.ioc.RealTypesModule;
import com.intuso.housemate.client.v1_0.real.impl.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class NodeRootModule extends AbstractModule {

    private final String nodeId;

    public NodeRootModule(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    protected void configure() {

        // install other required modules
        install(new AnnotationParserV1_0Module());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealNode.class).to(RealNodeImpl.class);
        bind(RealNodeImpl.class).in(Scopes.SINGLETON);

        bind(RealHardware.Container.class).to(RealNodeImpl.class);

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealNodeImpl.Service.class);
    }

    @Provides
    @Node
    public String getNodeId() {
        return nodeId;
    }

    @Provides
    @Node
    public Logger getRootLogger(@Node String nodeId) {
        return ChildUtil.logger(LoggerFactory.getLogger(RealObject.REAL), Object.VERSION, Server.NODES_ID, nodeId);
    }

    @Provides
    @Type
    public Logger getTypesLogger(@Node Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
