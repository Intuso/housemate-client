package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.api.object.Node;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.HardwareDetectorPluginListener;
import com.intuso.housemate.client.v1_0.real.impl.RealNodeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealObject;
import com.intuso.housemate.client.v1_0.real.impl.annotation.ioc.AnnotationParserV1_0Module;
import com.intuso.housemate.client.v1_0.real.impl.type.ioc.RealTypesModule;
import com.intuso.housemate.client.v1_0.real.impl.utils.ioc.RealUtilsModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 */
public class NodeRootModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        String defaultId = UUID.randomUUID().toString();
        defaultProperties.set(RealNodeImpl.NODE_ID, defaultId);
        defaultProperties.set(RealNodeImpl.NODE_NAME, defaultId);
        defaultProperties.set(RealNodeImpl.NODE_DESCRIPTION, defaultId);
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

        // bind hardware detector NB this comes after the types listener binding so the driver should be available already
        bind(HardwareDetectorPluginListener.class).in(Scopes.SINGLETON);

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealNodeImpl.Service.class);
    }

    @Provides
    @Type
    public Logger getTypesLogger() {
        return ChildUtil.logger(LoggerFactory.getLogger(RealObject.REAL), Object.VERSION, Server.NODES_ID, "somenode", Node.TYPES_ID);
    }
}
