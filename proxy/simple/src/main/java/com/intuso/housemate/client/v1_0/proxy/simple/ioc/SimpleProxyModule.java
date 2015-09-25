package com.intuso.housemate.client.v1_0.proxy.simple.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.client.v1_0.proxy.simple.*;
import com.intuso.housemate.comms.v1_0.api.payload.*;

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
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ApplicationData, SimpleProxyApplication>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ApplicationInstanceData, SimpleProxyApplicationInstance>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<AutomationData, SimpleProxyAutomation>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<CommandData, SimpleProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ConditionData, SimpleProxyCondition>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<DeviceData, SimpleProxyDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<HardwareData, SimpleProxyHardware>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<OptionData, SimpleProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ParameterData, SimpleProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<PropertyData, SimpleProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ServerData, SimpleProxyServer>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SubTypeData, SimpleProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<TaskData, SimpleProxyTask>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<TypeData<HousemateData<?>>, SimpleProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<UserData, SimpleProxyUser>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ValueData, SimpleProxyValue>>() {}));
        bind(new TypeLiteral<ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).to(SimpleProxyFactory.class);
        bind(new TypeLiteral<ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyDevice>>() {}).to(SimpleProxyFeatureFactory.class);
        bind(SimpleProxyRoot.class).in(Scopes.SINGLETON);
    }
}
