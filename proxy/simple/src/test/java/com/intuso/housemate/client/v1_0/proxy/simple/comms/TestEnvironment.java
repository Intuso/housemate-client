package com.intuso.housemate.client.v1_0.proxy.simple.comms;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.proxy.simple.TestProxyRoot;
import com.intuso.housemate.client.v1_0.proxy.simple.TestRealRoot;
import com.intuso.housemate.client.v1_0.proxy.simple.ioc.SimpleProxyModule;
import com.intuso.housemate.client.v1_0.real.impl.ioc.RealObjectModule;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.object.v1_0.api.RegexMatcher;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.junit.Ignore;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 */
@Ignore
public class TestEnvironment {

    public final static ApplicationDetails APP_DETAILS = new ApplicationDetails("test", "Test", "Test");
    public final static TestEnvironment TEST_INSTANCE = new TestEnvironment();

    private final Injector injector;
    private final TestRealRoot realRoot;
    private final TestProxyRoot proxyRoot;

    public TestEnvironment() {

        ListenersFactory listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        injector = Guice.createInjector(new TestModule(listenersFactory), new SimpleProxyModule(), new RealObjectModule());

        realRoot = injector.getInstance(TestRealRoot.class);
        proxyRoot = injector.getInstance(TestProxyRoot.class);
        injector.getInstance(RealRouterImpl.class).setProxyRoot(proxyRoot);
        injector.getInstance(ProxyRouterImpl.class).setRealRoot(realRoot);
        realRoot.init();
    }

    public Injector getInjector() {
        return injector;
    }

    public TestRealRoot getRealRoot() {
        return realRoot;
    }

    public TestProxyRoot getProxyRoot() {
        return proxyRoot;
    }

    private class RM implements RegexMatcher {

        Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}
