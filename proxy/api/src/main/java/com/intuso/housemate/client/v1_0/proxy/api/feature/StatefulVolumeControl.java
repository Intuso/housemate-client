package com.intuso.housemate.client.v1_0.proxy.api.feature;

import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Value;

/**
 * Interface for all devices that allow volume control with state
 * @param <COMMAND> the command type
 * @param <VALUE> the value type
 */
public interface StatefulVolumeControl<COMMAND extends Command<?, ?, ?, ?>, VALUE extends Value<?, ?>>
        extends VolumeControl<COMMAND> {

    String ID = "volume-stateful";

    /**
     * Get the value for the current volume
     * @return the value for the current volume
     */
    VALUE getCurrentVolumeValue();
}
