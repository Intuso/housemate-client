package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 */
public interface RealCommand<
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        PARAMETERS extends RealList<? extends RealParameter<?, ?, ?>, ?>,
        COMMAND extends RealCommand<BOOLEAN_VALUE, PARAMETERS, COMMAND>>
        extends Command<Type.InstanceMap, BOOLEAN_VALUE, PARAMETERS, COMMAND> {

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    void perform(Type.InstanceMap values);

    interface Performer {
        void perform(Type.InstanceMap values);
    }
}
