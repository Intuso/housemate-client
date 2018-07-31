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
            String destination = null;
            try {
                destination = message != null && message.getJMSDestination() != null ? message.getJMSDestination().toString() : "unknown";
                if (message instanceof StreamMessage) {
                    StreamMessage streamMessage = (StreamMessage) message;
                    Object messageObject = streamMessage.readObject();
                    if (messageObject instanceof byte[])
                        return javabinSerialiser.deserialise((byte[]) messageObject, tClass);
                    else
                        throw new MessagingException("Received message data on " + destination + " that was not a " + byte[].class.getName());
                } else
                    throw new MessagingException("Received message on " + destination + " that wasn't a " + StreamMessage.class.getName() + " but a " + message.getClass().getName());
            } catch (JMSException e) {
                if(destination == null)
                    throw new MessagingException("Could not get destination from message", e);
                else
                    throw new MessagingException("Could not read object from message received on " + destination, e);
            }
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
            String destination = null;
            try {
                destination = message != null && message.getJMSDestination() != null ? message.getJMSDestination().toString() : "unknown";
                if (message instanceof TextMessage) {
                    return jsonSerialiser.deserialise(((TextMessage) message).getText(), tClass);
                } else if (message instanceof BytesMessage) {
                    BytesMessage bytesMessage = (BytesMessage) message;
                    byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(bytes);
                    return jsonSerialiser.deserialise(new String(bytes), tClass);
                } else
                    throw new MessagingException("Received message on " + destination + " that wasn't a " + StreamMessage.class.getName() + " but a " + message.getClass().getName());
            } catch (JMSException e) {
                if(destination == null)
                    throw new MessagingException("Could not get destination from message", e);
                else
                    throw new MessagingException("Could not read object from message received on " + destination, e);
            }
        }
    }
}
