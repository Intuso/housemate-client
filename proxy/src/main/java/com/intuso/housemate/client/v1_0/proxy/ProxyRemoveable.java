package com.intuso.housemate.client.v1_0.proxy;

import com.intuso.housemate.client.v1_0.api.Removeable;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface ProxyRemoveable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> extends Removeable<REMOVE_COMMAND> {
    void loadRemoveCommand(CommandView commandView);
}
