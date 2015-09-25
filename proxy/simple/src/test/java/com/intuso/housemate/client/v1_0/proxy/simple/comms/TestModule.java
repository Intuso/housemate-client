package com.intuso.housemate.client.v1_0.proxy.simple.comms;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.client.v1_0.proxy.simple.TestProxyRoot;
import com.intuso.housemate.client.v1_0.proxy.simple.TestRealRoot;
import com.intuso.housemate.client.v1_0.real.api.RealRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.object.v1_0.api.RegexMatcher;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 06/01/14
 * Time: 08:26
 * To change this template use File | Settings | File Templates.
 */
public class TestModule extends AbstractModule {

    private final ListenersFactory listenersFactory;

    public TestModule(ListenersFactory listenersFactory) {
        this.listenersFactory = listenersFactory;
    }

    @Override
    protected void configure() {
        bind(PropertyRepository.class).toInstance(WriteableMapPropertyRepository.newEmptyRepository(listenersFactory));
        bind(RealRoot.class).to(TestRealRoot.class).in(Scopes.SINGLETON);
        bind(ProxyRoot.class).to(TestProxyRoot.class).in(Scopes.SINGLETON);
        bind(Router.class).to(ProxyRouterImpl.class);
        bind(Router.class).annotatedWith(Proxy.class).to(ProxyRouterImpl.class);
        bind(Router.class).annotatedWith(Real.class).to(RealRouterImpl.class);
        bind(RealRouterImpl.class).in(Scopes.SINGLETON);
        bind(ProxyRouterImpl.class).in(Scopes.SINGLETON);
        bind(RegexMatcher.Factory.class).toInstance(new RegexMatcher.Factory() {
            @Override
            public RegexMatcher createRegexMatcher(final String pattern) {
                return new RegexMatcher() {
                    @Override
                    public boolean matches(String string) {
                        return Pattern.compile(pattern).matcher(string).matches();
                    }
                };
            }
        });
        bind(ListenersFactory.class).toInstance(new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(Lists.<LISTENER>newArrayList());
            }
        });
    }

    @Provides
    @Singleton
    public Log getLog() {
        Log log = new Log();
        log.addWriter(new StdOutWriter(LogLevel.DEBUG));
        return log;
    }
}
