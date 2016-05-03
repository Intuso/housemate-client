package com.intuso.housemate.client.v1_0.proxy.simple.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.feature.ProxyFeature;
import com.intuso.housemate.client.v1_0.proxy.api.object.feature.ProxyFeatureFactory;
import com.intuso.housemate.client.v1_0.proxy.simple.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyAutomation>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyCondition>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyFeature>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyHardware>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<ProxyObject<?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyServer>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyTask>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyUser>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyValue>>() {}));
        bind(new TypeLiteral<ProxyFeatureFactory<SimpleProxyFeature, ProxyFeature>>() {}).to(SimpleProxyFeatureFactory.class);
        bind(SimpleProxyRoot.class).in(Scopes.SINGLETON);
    }
}
