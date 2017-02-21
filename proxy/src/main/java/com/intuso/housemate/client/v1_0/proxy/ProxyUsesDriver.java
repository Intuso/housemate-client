package com.intuso.housemate.client.v1_0.proxy;

import com.intuso.housemate.client.v1_0.api.UsesDriver;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.Value;

/**
 * Classes implementing this are runnable objects
 * @param <DRIVER_LOADED_VALUE> the type of the value
 */
public interface ProxyUsesDriver<DRIVER_PROPERTY extends Property<?, ?, ?, ?>, DRIVER_LOADED_VALUE extends Value<?, ?, ?>> extends UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE> {
    boolean isDriverLoaded();
}