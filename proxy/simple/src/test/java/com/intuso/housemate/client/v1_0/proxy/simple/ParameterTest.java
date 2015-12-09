package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.utilities.listener.ListenersFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 */
public class ParameterTest {

    public final static String PARAMETER = "parameters";

    private SimpleProxyList<ParameterData, SimpleProxyParameter> proxyList
            = new SimpleProxyList<>(
            LoggerFactory.getLogger(ParameterTest.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(new Key<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}),
            new ListData<ParameterData>(PARAMETER, PARAMETER, PARAMETER));
    private RealListImpl<ParameterData, RealParameterImpl<?>> realList = new RealListImpl<>(
            LoggerFactory.getLogger(ParameterTest.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            PARAMETER, PARAMETER, PARAMETER, new ArrayList<RealParameterImpl<?>>());
    private RealParameterImpl realParameter;
    private SimpleProxyParameter proxyParameter;

    public ParameterTest() {
    }

    @Before
    public void addLists() {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realParameter = BooleanType.createParameter(
                LoggerFactory.getLogger(ParameterTest.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-parameter", "My Parameter", "description");
        realList.add(realParameter);
        proxyParameter = proxyList.get("my-parameter");
    }

    @Test
    public void testCreateParameter() {
        Assert.assertNotNull(proxyParameter);
        Assert.assertNotNull(proxyParameter.getTypeId());
    }
}
