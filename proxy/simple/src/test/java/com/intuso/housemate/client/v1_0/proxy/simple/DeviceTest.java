package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.object.v1_0.api.*;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

/**
 */
public class DeviceTest {

    public final static String DEVICES = "devices";

    private final static Command.PerformListener<SimpleProxyCommand> EMPTY_LISTENER = new Command.PerformListener<SimpleProxyCommand>() {
        @Override
        public void commandStarted(SimpleProxyCommand function) {}

        @Override
        public void commandFinished(SimpleProxyCommand function) {}

        @Override
        public void commandFailed(SimpleProxyCommand function, String error) {}
    };

    private SimpleProxyList<DeviceData, SimpleProxyDevice> proxyList
            = new SimpleProxyList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(new Key<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}),
            new ListData<DeviceData>(DEVICES, DEVICES, DEVICES));
    private RealList<DeviceData, RealDevice> realList = new RealList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            DEVICES, DEVICES, DEVICES, new ArrayList<RealDevice>());
    private RealDevice realPrimary;
    private SimpleProxyDevice proxyPrimary;

    public DeviceTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realPrimary = new RealDevice(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "test",
                new DeviceData("my-primary", "My Primary", "description"));
        realList.add(realPrimary);
        proxyPrimary = proxyList.get("my-primary");
    }

    @Test
    public void testCreateProxyDevice() {
        Assert.assertNotNull(proxyPrimary);
    }

    @Test
    public void testStartStopPrimary() {
        Assert.assertFalse(proxyPrimary.isRunning());
        realPrimary.getRunningValue().setTypedValues(Boolean.TRUE);
        Assert.assertTrue(proxyPrimary.isRunning());
        realPrimary.getRunningValue().setTypedValues(Boolean.FALSE);
        Assert.assertFalse(proxyPrimary.isRunning());
        proxyPrimary.getStartCommand().perform(EMPTY_LISTENER);
        Assert.assertTrue(proxyPrimary.isRunning());
        proxyPrimary.getStopCommand().perform(EMPTY_LISTENER);
        Assert.assertFalse(proxyPrimary.isRunning());
    }

    @Test
    public void testError() {
        Assert.assertNull(proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValues("error");
        assertEquals("error", proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValues((String)null);
        Assert.assertNull(proxyPrimary.getError());
    }

    @Test
    public void testListener() {
        final AtomicBoolean connectedUpdated = new AtomicBoolean(false);
        final AtomicBoolean nameUpdated = new AtomicBoolean(false);
        final AtomicBoolean runningUpdated = new AtomicBoolean(false);
        final AtomicBoolean errorUpdated = new AtomicBoolean(false);
        proxyPrimary.addObjectListener(new Device.Listener<SimpleProxyDevice>() {

            @Override
            public void renamed(SimpleProxyDevice primaryObject, String oldName, String newName) {
                nameUpdated.set(true);
            }

            @Override
            public void error(SimpleProxyDevice entityWrapper, String description) {
                runningUpdated.set(true);
            }

            @Override
            public void running(SimpleProxyDevice entityWrapper, boolean running) {
                errorUpdated.set(true);
            }
        });
        TypeInstanceMap values = new TypeInstanceMap();
        values.getChildren().put(DeviceData.NAME_ID, new TypeInstances(new TypeInstance("newName")));
        proxyPrimary.getRenameCommand().perform(values, EMPTY_LISTENER);
        proxyPrimary.getStartCommand().perform(EMPTY_LISTENER);
        proxyPrimary.getStopCommand().perform(EMPTY_LISTENER);
        realPrimary.getErrorValue().setTypedValues("error");
        proxyPrimary.getRemoveCommand().perform(EMPTY_LISTENER);
//        assertTrue(connectedUpdated.get());
        Assert.assertTrue(nameUpdated.get());
        Assert.assertTrue(errorUpdated.get());
        Assert.assertTrue(runningUpdated.get());
    }
}
