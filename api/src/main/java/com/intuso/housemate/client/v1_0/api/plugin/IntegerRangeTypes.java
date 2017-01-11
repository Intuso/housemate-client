package com.intuso.housemate.client.v1_0.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the integer range types that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IntegerRangeTypes {

    /**
     * The list of the integer range types the plugin provides
     * @return the list of the integer range types the plugin provides
     */
    IntegerRangeType[] value();

}
