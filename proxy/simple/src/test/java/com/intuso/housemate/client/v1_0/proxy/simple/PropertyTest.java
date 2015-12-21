package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Property;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class PropertyTest {

    public final static String PROPERTIES = "properties";

    private SimpleProxyList<PropertyData, SimpleProxyProperty> proxyList
            = new SimpleProxyList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(new Key<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}),
            LoggerFactory.getLogger(PropertyTest.class),
            new ListData<PropertyData>(PROPERTIES, PROPERTIES, PROPERTIES));
    private RealListImpl<PropertyData, RealPropertyImpl<?>> realList = new RealListImpl<>(
            LoggerFactory.getLogger(PropertyTest.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            PROPERTIES, PROPERTIES, PROPERTIES, new ArrayList<RealPropertyImpl<?>>());
    private RealPropertyImpl<Integer> realProperty;
    private SimpleProxyProperty proxyProperty;

    public PropertyTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realProperty = IntegerType.createProperty(
                LoggerFactory.getLogger(PropertyTest.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-property", "My Property", "description", Arrays.asList(1234));
        realList.add(realProperty);
        proxyProperty = proxyList.get("my-property");
    }

    @Test
    public void testCreateProxyProperty() {
        Assert.assertNotNull(proxyProperty);
    }

    @Test
    public void testSetProxyProperty() {
        proxyProperty.set(new TypeInstances(new TypeInstance("-1234")), new Command.PerformListener<SimpleProxyCommand>() {
            @Override
            public void commandStarted(SimpleProxyCommand function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFinished(SimpleProxyCommand function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFailed(SimpleProxyCommand function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Assert.assertEquals(-1234, (int) realProperty.getTypedValue());
    }

    @Test
    public void testListenerCalled() {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyProperty.addObjectListener(new Property.Listener<SimpleProxyProperty>() {

            @Override
            public void valueChanging(SimpleProxyProperty value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyProperty property) {
                called.set(true);
            }
        });
        realProperty.setTypedValues(-1234);
        Assert.assertEquals(true, called.get());
    }
}
