package com.intuso.housemate.client.v1_0.real.impl.type.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.api.plugin.PluginListener;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.type.*;

/**
 * Created by tomc on 13/05/16.
 */
public class RealTypesModule extends AbstractModule {

    @Override
    protected void configure() {

        // other type factories
        install(new FactoryModuleBuilder().build(EnumChoiceType.Factory.class));
        install(new FactoryModuleBuilder().build(RealCompositeType.Factory.class));
        install(new FactoryModuleBuilder().build(RealRegexType.Factory.class));

        bind(ConditionDriverType.class).in(Scopes.SINGLETON);
        bind(FeatureDriverType.class).in(Scopes.SINGLETON);
        bind(HardwareDriverType.class).in(Scopes.SINGLETON);
        bind(TaskDriverType.class).in(Scopes.SINGLETON);
        bind(TypeRepository.class).in(Scopes.SINGLETON);
        bind(TypeSerialiser.Repository.class).to(TypeRepository.class);

        // bind implementations
        bind(new TypeLiteral<TypeSerialiser<RealObjectType.Reference<?>>>() {}).to(new Key<RealObjectType.Serialiser<?>>() {}).in(Scopes.SINGLETON);

        // bind driver plugin listener
        bind(TypesPluginsListener.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), PluginListener.class).addBinding().to(TypesPluginsListener.class);
    }
}
