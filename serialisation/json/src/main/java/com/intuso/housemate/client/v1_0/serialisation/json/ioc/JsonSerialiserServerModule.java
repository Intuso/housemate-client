package com.intuso.housemate.client.v1_0.serialisation.json.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.v1_0.serialisation.api.Serialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiserServerModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), Serialiser.class).addBinding().to(JsonSerialiser.class);
    }
}
