package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Feature;
import org.slf4j.Logger;

/**
 * Base class for all devices
 */
public interface RealFeature<COMMANDS extends RealList<? extends RealCommand<?, ?, ?>>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>>,
        FEATURE extends RealFeature<COMMANDS, VALUES, FEATURE>>
        extends Feature<COMMANDS,
        VALUES,
        FEATURE> {

    interface Factory<FEATURE extends RealFeature<?, ?, ?>> {
        FEATURE create(Logger logger, Data data);
    }
}
