package com.intuso.housemate.client.v1_0.serialisation.json.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.intuso.housemate.client.v1_0.serialisation.api.Serialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiserModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Serialiser.class).annotatedWith(Names.named(JsonSerialiser.TYPE)).to(JsonSerialiser.class);
        bind(JsonSerialiser.class).in(Scopes.SINGLETON);
    }
}
