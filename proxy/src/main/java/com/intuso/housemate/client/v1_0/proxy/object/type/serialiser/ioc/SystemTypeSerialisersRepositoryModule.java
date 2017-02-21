package com.intuso.housemate.client.v1_0.proxy.object.type.serialiser.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.proxy.object.type.serialiser.SystemTypeSerialisersRepository;

/**
 * Created by tomc on 12/01/17.
 */
public class SystemTypeSerialisersRepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TypeSerialiser.Repository.class).to(SystemTypeSerialisersRepository.class);
        bind(SystemTypeSerialisersRepository.class).in(Scopes.SINGLETON);
    }
}
