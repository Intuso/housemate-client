package com.intuso.housemate.client.v1_0.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the hardware factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HardwareDriver {

    /**
     * The list of the hardware factories the plugin provides
     * @return the list of the hardware factories the plugin provides
     */
    Class<? extends com.intuso.housemate.client.v1_0.api.driver.HardwareDriver> value();
    Class<? extends com.intuso.housemate.client.v1_0.api.driver.HardwareDriver.Detector> detector() default com.intuso.housemate.client.v1_0.api.driver.HardwareDriver.DummyDetector.class;
}
