package com.intuso.housemate.client.v1_0.messaging.api;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by tomc on 01/12/16.
 */
public interface Receiver<T extends Serializable> {

    void close();
    T getMessage();
    Iterator<T> getMessages();
    void listen(Listener<T> listener);

    interface Listener<T extends Serializable> {
        void onMessage(T t, boolean wasPersisted);
    }

    interface Factory {
        <T extends Serializable> Receiver<T> create(Logger logger, String name, Class<T> tClass);
    }
}
