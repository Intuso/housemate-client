package com.intuso.housemate.client.v1_0.real.impl.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.*;

import java.util.Map;

/**
 * Created by tomc on 04/05/16.
 */
public class RealTypesModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(new TypeLiteral<Map<String, RealTypeImpl.Factory<?>>>() {}).toProvider(TypeFactoriesProvider.class);

        // bind everything as singletons that should be
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(EmailType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);
    }
}
