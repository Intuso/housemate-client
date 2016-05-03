package com.intuso.housemate.client.v1_0.api;

import com.intuso.housemate.client.v1_0.api.object.Command;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface Removeable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> {

    String REMOVE_ID = "remove";

    /**
     * Gets the remove command
     * @return the remove command
     */
    REMOVE_COMMAND getRemoveCommand();
}
