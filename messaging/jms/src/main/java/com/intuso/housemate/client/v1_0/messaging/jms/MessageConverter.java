package com.intuso.housemate.client.v1_0.messaging.jms;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.messaging.api.MessagingException;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by tomc on 02/03/17.
 */
public interface MessageConverter {
    Message toMessage(Session session, Serializable object) throws JMSException;
    <T extends Serializable> T fromMessage(Message message, Class<T> tClass);

    class Javabin implements MessageConverter {

        private final JavabinSerialiser javabinSerialiser;

        @Inject
        public Javabin(JavabinSerialiser javabinSerialiser) {
            this.javabinSerialiser = javabinSerialiser;
        }

        @Override
        public Message toMessage(Session session, Serializable object) throws JMSException {
            StreamMessage message = session.createStreamMessage();
            message.writeBytes(javabinSerialiser.serialise(object));
            return message;
        }

        @Override
        public <T extends Serializable> T fromMessage(Message message, Class<T> tClass) {
            if (message instanceof StreamMessage) {
                StreamMessage streamMessage = (StreamMessage) message;
                try {
                    Object messageObject = streamMessage.readObject();
                    if (messageObject instanceof byte[])
                        return javabinSerialiser.deserialise((byte[]) messageObject, tClass);
                    else
                        throw new MessagingException("Message data was not a " + byte[].class.getName());
                } catch (JMSException e) {
                    throw new MessagingException("Could not read object from received message", e);
                }
            } else
                throw new MessagingException("Received message that wasn't a " + StreamMessage.class.getName() + " but a " + message.getClass().getName());
        }
    }

    class Json implements MessageConverter {

        private final JsonSerialiser jsonSerialiser;

        @Inject
        public Json(JsonSerialiser jsonSerialiser) {
            this.jsonSerialiser = jsonSerialiser;
        }

        @Override
        public Message toMessage(Session session, Serializable object) throws JMSException {
            return session.createTextMessage(jsonSerialiser.serialise(object));
        }

        @Override
        public <T extends Serializable> T fromMessage(Message message, Class<T> tClass) {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    return jsonSerialiser.deserialise(textMessage.getText(), tClass);
                } catch (JMSException e) {
                    throw new MessagingException("Could not read object from received message", e);
                }
            } else
                throw new MessagingException("Received message that wasn't a " + StreamMessage.class.getName() + " but a " + message.getClass().getName());
        }
    }
}
