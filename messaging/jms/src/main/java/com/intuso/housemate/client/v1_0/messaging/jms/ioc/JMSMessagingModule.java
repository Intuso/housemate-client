package com.intuso.housemate.client.v1_0.messaging.jms.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.messaging.api.ioc.Messaging;
import com.intuso.housemate.client.v1_0.messaging.jms.JMS;
import com.intuso.housemate.client.v1_0.messaging.jms.MessageConverter;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.javabin.ioc.JavabinSerialiserModule;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.ioc.JsonSerialiserModule;

import javax.jms.Connection;

/**
 * Created by tomc on 02/03/17.
 */
public class JMSMessagingModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JavabinSerialiserModule());
        bind(MessageConverter.Javabin.class).in(Scopes.SINGLETON);
        install(new JsonSerialiserModule());
        bind(MessageConverter.Json.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public Receiver.Factory getDefaultReceiver(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Receiver.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    public Sender.Factory getDefaultSender(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Sender.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.TYPE)
    public Receiver.Factory getJavabinReceiver(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Receiver.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.TYPE)
    public Sender.Factory getJavabinSender(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Sender.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JsonSerialiser.TYPE)
    public Receiver.Factory getJsonReceiver(MessageConverter.Json messageConverter, Connection connection) {
        return new JMS.Receiver.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JsonSerialiser.TYPE)
    public Sender.Factory getJsonSender(MessageConverter.Json messageConverter, Connection connection) {
        return new JMS.Sender.FactoryImpl(messageConverter, connection);
    }
}
