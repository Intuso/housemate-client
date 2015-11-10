package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class CommandTest {

    public final static String COMMANDS = "commands";

    private static Command.PerformListener<SimpleProxyCommand> EMPTY_FUNCTION_LISTENER = new Command.PerformListener<SimpleProxyCommand>() {
        @Override
        public void commandStarted(SimpleProxyCommand function) {}

        @Override
        public void commandFinished(SimpleProxyCommand function) {}

        @Override
        public void commandFailed(SimpleProxyCommand function, String error) {}
    };

    private SimpleProxyList<CommandData, SimpleProxyCommand> proxyList
            = new SimpleProxyList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(new Key<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}),
            new ListData<CommandData>(COMMANDS, COMMANDS, COMMANDS));
    private RealListImpl<CommandData, RealCommandImpl> realList = new RealListImpl<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            COMMANDS, COMMANDS, COMMANDS, new ArrayList<RealCommandImpl>());
    private RealCommandImpl realCommand;
    private SimpleProxyCommand proxyCommand;

    public CommandTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realCommand = new RealCommandImpl(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-command", "My Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstanceMap values) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        realList.add(realCommand);
        proxyCommand = proxyList.get("my-command");
    }

    @Test
    public void testCreateProxyCommand() {
        Assert.assertNotNull(proxyCommand);
    }

    @Test
    public void testPerformProxyFunction() {
        final AtomicBoolean called = new AtomicBoolean(false);
        RealCommandImpl realCommand = new RealCommandImpl(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-other-command", "My Other Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstanceMap values) {
                called.set(true);
            }
        };
        realList.add(realCommand);
        SimpleProxyCommand proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap(), EMPTY_FUNCTION_LISTENER);
        Assert.assertEquals(true, called.get());
    }

    @Test
    public void testParameter() {
        final AtomicBoolean correctParam = new AtomicBoolean(false);
        RealCommandImpl realCommand = new RealCommandImpl(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-other-command", "My Other Command",
                "description", Arrays.<RealParameter<?>>asList(
                        IntegerType.createParameter(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                                "my-parameter", "My Parameter", "description"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                correctParam.set(values.getChildren().get("my-parameter") != null
                        && values.getChildren().get("my-parameter").getFirstValue() != null
                        && values.getChildren().get("my-parameter").getFirstValue().equals("1234"));
            }
        };
        realList.add(realCommand);
        SimpleProxyCommand proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap() {
            {
                getChildren().put("my-parameter", new TypeInstances(new TypeInstance("1234")));
            }
        }, EMPTY_FUNCTION_LISTENER);
        Assert.assertEquals(true, correctParam.get());
    }

    @Test
    public void testPerformListenerCalled() {
        final AtomicBoolean functionStartedCalled = new AtomicBoolean(false);
        final AtomicBoolean functionFinishedCalled = new AtomicBoolean(false);
        proxyCommand.perform(new TypeInstanceMap(), new Command.PerformListener<SimpleProxyCommand>() {
            @Override
            public void commandStarted(SimpleProxyCommand function) {
                functionStartedCalled.set(true);
            }

            @Override
            public void commandFinished(SimpleProxyCommand function) {
                functionFinishedCalled.set(true);
            }

            @Override
            public void commandFailed(SimpleProxyCommand function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Assert.assertEquals(true, functionStartedCalled.get());
        Assert.assertEquals(true, functionFinishedCalled.get());
    }
}
