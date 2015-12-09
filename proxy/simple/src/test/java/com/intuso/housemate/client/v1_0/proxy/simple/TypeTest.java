package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRegexType;
import com.intuso.housemate.client.v1_0.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.client.v1_0.real.impl.type.RealRegexType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData;
import com.intuso.housemate.object.v1_0.api.RegexMatcher;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 */
public class TypeTest {

    @Test
    public void testStringType() {
        TypeSerialiser<String> serialiser = StringType.SERIALISER;
        String value = "value";
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("value", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testIntegerType() {
        TypeSerialiser<Integer> serialiser = IntegerType.SERIALISER;
        Integer value = 1234;
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("1234", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
        value = -1234;
        serialised = serialiser.serialise(value);
        assertEquals("-1234", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testBooleanType() {
        TypeSerialiser<Boolean> serialiser = BooleanType.SERIALISER;
        Boolean value = Boolean.TRUE;
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("true", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomType() {
        TypeSerialiser<MyClass> serialiser = SERIALISER;
        MyClass mc = new MyClass("one", "two");
        TypeInstance serialised = serialiser.serialise(mc);
        assertEquals("one;two", serialised.getValue());
        assertEquals(mc, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomFormat() {
        ProxyRegexType pt = new MyProxyType(
                LoggerFactory.getLogger(TypeTest.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                TestEnvironment.TEST_INSTANCE.getInjector(),
                new MyRealType(LoggerFactory.getLogger(TypeTest.class),
                        TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class)).getData());
        Assert.assertFalse(pt.isCorrectFormat("some string"));
        Assert.assertFalse(pt.isCorrectFormat(";two"));
        Assert.assertFalse(pt.isCorrectFormat("one;"));
        Assert.assertFalse(pt.isCorrectFormat("one;two;three"));
        Assert.assertTrue(pt.isCorrectFormat("one;two"));
        assertEquals(DESCRIPTION, pt.getDescription());
    }

    private class MyClass {
        String field1;
        String field2;

        private MyClass(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        @Override
        public boolean equals(Object otherObject) {
            MyClass otherMyClass = (MyClass)otherObject;
            return otherMyClass.field1.equals(field1) && otherMyClass.field2.equals(field2);
        }
    }

    private TypeSerialiser<MyClass> SERIALISER = new TypeSerialiser<MyClass>() {
        @Override
        public TypeInstance serialise(MyClass myClass) {
            return new TypeInstance(myClass.field1 + ";" + myClass.field2);
        }

        @Override
        public MyClass deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            String[] parts = value.getValue().split(";");
            return new MyClass(parts[0], parts[1]);
        }
    };

    private final static String ID = "my-type";
    private final static String NAME = "My Type";
    private final static String DESCRIPTION = "Two pieces of text without the character ';', separated by a ';'";
    private final static String REGEX = "[^;]+;[^;]+";

    private class MyRealType extends RealRegexType<MyClass> {

        protected MyRealType(Logger logger, ListenersFactory listenersFactory) {
            super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1, REGEX);
        }

        @Override
        public TypeInstance serialise(MyClass o) {
            return SERIALISER.serialise(o);
        }

        @Override
        public MyClass deserialise(TypeInstance value) {
            return SERIALISER.deserialise(value);
        }
    }

    private class MyProxyType extends ProxyRegexType<MyProxyType> {

        @Inject
        public MyProxyType(Logger logger, ListenersFactory listenersFactory, Injector injector, RegexTypeData data) {
            super(logger, listenersFactory, data, injector.getInstance(RegexMatcher.Factory.class).createRegexMatcher(data.getRegexPattern()));
        }

        @Override
        protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
            return null;
        }
    }
}
