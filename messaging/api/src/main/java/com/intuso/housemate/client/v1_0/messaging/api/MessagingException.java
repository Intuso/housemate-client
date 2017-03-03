package com.intuso.housemate.client.v1_0.messaging.api;

/**
 * Created by tomc on 21/02/17.
 */
public class MessagingException extends RuntimeException {

    public MessagingException() {}

    public MessagingException(String s) {
        super(s);
    }

    public MessagingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MessagingException(Throwable throwable) {
        super(throwable);
    }
}
