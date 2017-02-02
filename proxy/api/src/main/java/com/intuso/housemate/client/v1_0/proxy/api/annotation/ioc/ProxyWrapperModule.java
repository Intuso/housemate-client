package com.intuso.housemate.client.v1_0.proxy.api.annotation.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ClassCreator;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapperV1_0;

/**
 * Created by tomc on 11/01/17.
 */
public class ProxyWrapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProxyWrapper.class).to(ProxyWrapperV1_0.class);
        bind(ProxyWrapperV1_0.class).in(Scopes.SINGLETON);
        bind(ClassCreator.class).to(ClassCreator.FromInjector.class);
    }
}
