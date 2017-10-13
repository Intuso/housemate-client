package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.view.DeviceView;

/**
 * Base class for all devices
 */
public interface RealDevice<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        VIEW extends DeviceView<?>,
        DEVICE extends RealDevice<DATA, LISTENER, COMMAND, COMMANDS, VALUES, VIEW, DEVICE>>
        extends Device<DATA, LISTENER, COMMAND, COMMANDS, VALUES, VIEW, DEVICE> {}
