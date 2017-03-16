package com.intuso.housemate.client.v1_0.serialisation.json;

import com.google.gson.Gson;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.serialisation.api.Serialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.config.GsonConfig;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerialiser implements Serialiser<String> {

    public final static String TYPE = "application/json";

    private final Gson gson;

    public JsonSerialiser() {
        try {
            gson = GsonConfig.createGson();
        } catch(Throwable t) {
            throw new HousemateException("Failed to create json serialiser");
        }
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String serialise(Serializable object) {
        return gson.toJson(object);
    }

    @Override
    public <T extends Serializable> T deserialise(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }
}
