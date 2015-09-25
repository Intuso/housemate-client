package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Removeable;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface ProxyRemoveable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> extends Removeable<REMOVE_COMMAND> {

}
