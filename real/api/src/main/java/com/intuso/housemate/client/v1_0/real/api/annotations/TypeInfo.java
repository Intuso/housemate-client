package com.intuso.housemate.client.v1_0.real.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TypeInfo {
    String id();
    String name();
    String description();
}
