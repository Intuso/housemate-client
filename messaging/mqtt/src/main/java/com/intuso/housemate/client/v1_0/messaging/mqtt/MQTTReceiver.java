package com.intuso.housemate.client.v1_0.messaging.mqtt;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.messaging.api.MessagingException;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by tomc on 07/03/17.
 */
public class MQTTReceiver<T extends Serializable> implements Receiver<T> {

    private final Logger logger;
    private final String name;
    private final Class<T> tClass;
    private final MqttClient client;
    private final MessageConverter messageConverter;

    @Inject
    public MQTTReceiver(@Assisted Logger logger,
                        @Assisted String name,
                        @Assisted Class tClass,
                        MqttClient client,
                        MessageConverter messageConverter) {
        this.logger = logger;
        this.name = name;
        this.tClass = tClass;
        this.client = client;
        this.messageConverter = messageConverter;
    }

    @Override
    public void close() {
        // nothing to do for mqtt
    }

    @Override
    public T getMessage() {
        final MessageFuture result = new MessageFuture();
        listen(new Listener<T>() {
            @Override
            public void onMessage(T t, boolean wasPersisted) {
                result.set(t);
            }
        });
        try {
            return result.get();
        } catch (InterruptedException e) {
            logger.error("Interrupted waiting for message");
            return null;
        }
    }

    @Override
    public Iterator<T> getMessages() {
        return null; // todo
    }

    @Override
    public void listen(Listener<T> listener) {
        try {
            client.subscribe(name, 0, new ListenerWrapper(listener));
        } catch (MqttException e) {
            throw new MessagingException("Failed to subscribe to mqtt topic", e);
        }
    }

    private class MessageFuture implements Future<T> {

        private T result;

        @Override
        public boolean cancel(boolean b) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return result != null;
        }

        @Override
        public synchronized T get() throws InterruptedException {
            if(result != null)
                return result;
            wait();
            return result;
        }

        @Override
        public synchronized T get(long l, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
            if(result != null)
                return result;
            wait(timeUnit.toMillis(l));
            return result;
        }

        public synchronized void set(T result) {
            this.result = result;
            notifyAll();
        }
    }

    private class ListenerWrapper implements IMqttMessageListener {

        private final Listener<T> listener;

        private ListenerWrapper(Listener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            listener.onMessage(messageConverter.fromPayload(mqttMessage.getPayload(), tClass), mqttMessage.isRetained());
        }
    }

    public interface Factory {
        MQTTReceiver<?> create(Logger logger, String name, Class tClass);
    }

    public static class FactoryImpl implements Receiver.Factory {

        private final Factory factory;

        @Inject
        public FactoryImpl(Factory factory) {
            this.factory = factory;
        }

        @Override
        public <T extends Serializable> Receiver<T> create(Logger logger, String name, Class<T> tClass) {
            return (Receiver<T>) factory.create(logger, name, tClass);
        }
    }
}
