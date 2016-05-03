package com.intuso.housemate.client.v1_0.data.serialiser.json.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.v1_0.data.serialiser.api.Serialiser;
import com.intuso.housemate.client.v1_0.data.serialiser.json.JsonSerialiser;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiserClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Serialiser.class).to(JsonSerialiser.class);
    }
}
