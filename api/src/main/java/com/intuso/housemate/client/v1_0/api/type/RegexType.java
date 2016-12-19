package com.intuso.housemate.client.v1_0.api.type;

/**
 * Created by tomc on 17/05/16.
 */
public interface RegexType {

    String getValue();

    interface Factory<TYPE extends RegexType> {
        TYPE create(String value);
    }
}
