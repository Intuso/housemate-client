package com.intuso.housemate.client.v1_0.api.plugin;

import com.intuso.housemate.client.v1_0.api.annotation.Id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to describe a composite type
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChoiceType {
    Id id();
    Class<? extends Enum> options();
}
