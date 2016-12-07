package com.intuso.housemate.client.v1_0.proxy.simple.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SimpleProxyModule());
        bind(SimpleProxyServer.class).in(Scopes.SINGLETON);
        bind(ProxyServer.class).to(SimpleProxyServer.class);
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(ProxyServer.Service.class);
    }

    @Provides
    @Server
    public Logger getRootLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }
}
