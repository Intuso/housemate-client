package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

/**
 */
public class ValueTest {

    public final static String VALUES = "values";

    private SimpleProxyList<ValueData, SimpleProxyValue> proxyList
            = new SimpleProxyList<>(
            LoggerFactory.getLogger(ValueTest.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(new Key<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}),
            new ListData<ValueData>(VALUES, VALUES, VALUES));
    private RealListImpl<ValueBaseData<NoChildrenData>, RealValueImpl<?>> realList = new RealListImpl<>(
            LoggerFactory.getLogger(ValueTest.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            VALUES, VALUES, VALUES, new ArrayList<RealValueImpl<?>>());
    private RealValueImpl<Integer> realValue;
    private SimpleProxyValue proxyValue;

    public ValueTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realValue = IntegerType.createValue(
                LoggerFactory.getLogger(ValueTest.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-value", "My Value", "description", 1234);
        realList.add(realValue);
        proxyValue = proxyList.get("my-value");
    }

    @Test
    public void testCreateProxyValue() {
        Assert.assertNotNull(proxyValue);
    }

    @Test
    public void testSetRealValue() {
        realValue.setTypedValues(-1234);
        assertEquals("-1234", proxyValue.getValue().getFirstValue());
    }

    @Test
    public void testListenerCalled() {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyValue.addObjectListener(new Value.Listener<SimpleProxyValue>() {

            @Override
            public void valueChanging(SimpleProxyValue value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyValue value) {
                called.set(true);
            }
        });
        realValue.setTypedValues(-1234);
        assertEquals(true, called.get());
    }
}
