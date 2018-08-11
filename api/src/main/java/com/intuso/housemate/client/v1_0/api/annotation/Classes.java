package com.intuso.housemate.client.v1_0.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Classes {

    String FAN = "fan";
    String LIGHT = "light";
    String TV = "tv";

    String[] value();
}
