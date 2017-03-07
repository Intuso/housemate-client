package com.intuso.housemate.client.v1_0.messaging.mqtt;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * Created by tomc on 07/03/17.
 */
public class MQTTSender implements Sender {

    private final Logger logger;
    private final MqttTopic topic;
    private final MessageConverter messageConverter;

    @Inject
    public MQTTSender(@Assisted Logger logger,
                      @Assisted String name,
                      MqttClient client,
                      MessageConverter messageConverter) {
        this.logger = logger;
        this.topic = client.getTopic(name);
        this.messageConverter = messageConverter;
    }

    @Override
    public void close() {
        // nothing to do for mqtt
    }

    @Override
    public void send(Serializable object, boolean persistent) {
        try {
            byte[] bytes = messageConverter.toPayload(object);
            topic.publish(bytes, 1, persistent);
            logger.trace("Sent {} on {}", object, topic.getName());
        } catch(MqttException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public interface Factory {
        MQTTSender create(Logger logger, String name);
    }

    public static class FactoryImpl implements Sender.Factory {

        private final Factory factory;

        @Inject
        public FactoryImpl(Factory factory) {
            this.factory = factory;
        }

        @Override
        public Sender create(Logger logger, String name) {
            return factory.create(logger, name);
        }
    }
}
