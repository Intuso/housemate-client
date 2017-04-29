package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<
        LISTENER extends Device.Listener<? super DEVICE>,
        COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE extends RealDevice<LISTENER, COMMAND, COMMANDS, VALUES, DEVICE>>
        extends Device<LISTENER, COMMAND, COMMANDS, VALUES, DEVICE> {}
