package com.intuso.housemate.client.v1_0.messaging.mqtt;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Created by tomc on 02/03/17.
 */
public interface MessageConverter {

    byte[] toPayload(Serializable object) throws MqttException;
    <T extends Serializable> T fromPayload(byte[] payload, Class<T> tClass);

    class Javabin implements MessageConverter {

        private final JavabinSerialiser javabinSerialiser;

        @Inject
        public Javabin(JavabinSerialiser javabinSerialiser) {
            this.javabinSerialiser = javabinSerialiser;
        }

        @Override
        public byte[] toPayload(Serializable object) throws MqttException {
            return javabinSerialiser.serialise(object);
        }

        @Override
        public <T extends Serializable> T fromPayload(byte[] payload, Class<T> tClass) {
            return javabinSerialiser.deserialise(payload, tClass);
        }
    }

    class Json implements MessageConverter {

        private static Charset UTF8 = Charset.forName("UTF-8");

        private final JsonSerialiser jsonSerialiser;

        @Inject
        public Json(JsonSerialiser jsonSerialiser) {
            this.jsonSerialiser = jsonSerialiser;
        }

        @Override
        public byte[] toPayload(Serializable object) throws MqttException {
            return jsonSerialiser.serialise(object).getBytes(UTF8);
        }

        @Override
        public <T extends Serializable> T fromPayload(byte[] payload, Class<T> tClass) {
            return jsonSerialiser.deserialise(new String(payload, UTF8), tClass);
        }
    }
}
