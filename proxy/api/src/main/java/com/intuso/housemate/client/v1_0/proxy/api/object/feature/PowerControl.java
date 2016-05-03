package com.intuso.housemate.client.v1_0.proxy.api.object.feature;

import com.intuso.housemate.client.v1_0.api.object.Command;

/**
 * Interface for all devices that allow power control
 * @param <COMMAND> the command type
 */
public interface PowerControl<COMMAND extends Command<?, ?, ?, ?>> extends ProxyFeature {

    String ID = "power";

    /**
     * Get the command to power the device on
     * @return the command to power the device on
     */
    COMMAND getOnCommand();

    /**
     * Get the command to power the device off
     * @return the command to power the device off
     */
    COMMAND getOffCommand();
}
