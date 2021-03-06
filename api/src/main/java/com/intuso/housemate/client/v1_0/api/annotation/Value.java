package com.intuso.housemate.client.v1_0.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an interface method with this to create a value for your object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Value {

    /**
     * The value's type
     * @return the value's type
     */
    String restriction() default "";

    int minValues() default 0;

    int maxValues() default -1;
}
