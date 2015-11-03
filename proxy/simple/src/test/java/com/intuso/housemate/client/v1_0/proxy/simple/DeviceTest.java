package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.impl.RealDeviceImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
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
    private RealListImpl<DeviceData, RealDeviceImpl<?>> realList = new RealListImpl<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            DEVICES, DEVICES, DEVICES, new ArrayList<RealDeviceImpl<?>>());
    private RealDeviceImpl realPrimary;
    private SimpleProxyDevice proxyDevice;

    public DeviceTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realPrimary = new RealDeviceImpl(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                null,
                null,
                new DeviceData("my-primary", "My Primary", "description"),
                new RealDeviceImpl.RemovedListener() {
                    @Override
                    public void deviceRemoved(RealDeviceImpl device) {
                        // do nothing, just a test
                    }
                });
        realList.add(realPrimary);
        proxyDevice = proxyList.get("my-primary");
    }

    @Test
    public void testCreateProxyDevice() {
        Assert.assertNotNull(proxyDevice);
    }

    @Test
    public void testStartStopPrimary() {
        Assert.assertFalse(proxyDevice.isRunning());
        realPrimary.getRunningValue().setTypedValues(Boolean.TRUE);
        Assert.assertTrue(proxyDevice.isRunning());
        realPrimary.getRunningValue().setTypedValues(Boolean.FALSE);
        Assert.assertFalse(proxyDevice.isRunning());
        proxyDevice.getStartCommand().perform(EMPTY_LISTENER);
        Assert.assertTrue(proxyDevice.isRunning());
        proxyDevice.getStopCommand().perform(EMPTY_LISTENER);
        Assert.assertFalse(proxyDevice.isRunning());
    }

    @Test
    public void testError() {
        Assert.assertNull(proxyDevice.getError());
        realPrimary.getErrorValue().setTypedValues("error");
        assertEquals("error", proxyDevice.getError());
        realPrimary.getErrorValue().setTypedValues((String)null);
        Assert.assertNull(proxyDevice.getError());
    }

    @Test
    public void testListener() {
        final AtomicBoolean connectedUpdated = new AtomicBoolean(false);
        final AtomicBoolean nameUpdated = new AtomicBoolean(false);
        final AtomicBoolean runningUpdated = new AtomicBoolean(false);
        final AtomicBoolean driverLoadedUpdated = new AtomicBoolean(false);
        final AtomicBoolean errorUpdated = new AtomicBoolean(false);
        proxyDevice.addObjectListener(new Device.Listener<SimpleProxyDevice>() {

            @Override
            public void renamed(SimpleProxyDevice primaryObject, String oldName, String newName) {
                nameUpdated.set(true);
            }

            @Override
            public void error(SimpleProxyDevice entityWrapper, String description) {
                errorUpdated.set(true);
            }

            @Override
            public void driverLoaded(SimpleProxyDevice usesDriver, boolean loaded) {
                driverLoadedUpdated.set(true);
            }

            @Override
            public void running(SimpleProxyDevice entityWrapper, boolean running) {
                runningUpdated.set(true);
            }
        });
        TypeInstanceMap values = new TypeInstanceMap();
        values.getChildren().put(DeviceData.NAME_ID, new TypeInstances(new TypeInstance("newName")));
        proxyDevice.getRenameCommand().perform(values, EMPTY_LISTENER);
        proxyDevice.getStartCommand().perform(EMPTY_LISTENER);
        proxyDevice.getStopCommand().perform(EMPTY_LISTENER);
        realPrimary.getErrorValue().setTypedValues("error");
        proxyDevice.getRemoveCommand().perform(EMPTY_LISTENER);
//        assertTrue(connectedUpdated.get());
        Assert.assertTrue(nameUpdated.get());
        Assert.assertTrue(errorUpdated.get());
        Assert.assertTrue(runningUpdated.get());
    }
}
