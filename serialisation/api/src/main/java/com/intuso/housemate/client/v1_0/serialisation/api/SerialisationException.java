package com.intuso.housemate.client.v1_0.serialisation.api;

/**
 * Created by tomc on 21/02/17.
 */
public class SerialisationException extends RuntimeException {

    public SerialisationException() {}

    public SerialisationException(String s) {
        super(s);
    }

    public SerialisationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SerialisationException(Throwable throwable) {
        super(throwable);
    }
}
