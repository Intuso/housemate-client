package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.util.Modules;
import com.intuso.housemate.client.v1_0.real.api.RealApplication;
import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.client.v1_0.real.api.RealRoot;
import com.intuso.housemate.client.v1_0.real.api.RealUser;
import com.intuso.housemate.client.v1_0.real.impl.BasicRealRoot;
import com.intuso.housemate.client.v1_0.real.impl.ServerRealRoot;

/**
 */
public class ServerRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        install(Modules.override(new BasicRealObjectModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(RealRoot.class).to(ServerRealRoot.class);
                bind(BasicRealRoot.class).to(ServerRealRoot.class);
            }
        }));

        bind(ServerRealRoot.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(ServerRealRoot.class);
        bind(RealApplication.Container.class).to(ServerRealRoot.class);
        bind(RealUser.Container.class).to(ServerRealRoot.class);
    }
}
