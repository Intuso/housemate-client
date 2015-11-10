package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;

/**
 */
public interface RealCommand
        extends Command<TypeInstanceMap, RealValue<Boolean>, RealList<? extends RealParameter<?>>, RealCommand> {

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    void perform(TypeInstanceMap values);

    interface Factory {
        RealCommand create(CommandData data);
    }
}
