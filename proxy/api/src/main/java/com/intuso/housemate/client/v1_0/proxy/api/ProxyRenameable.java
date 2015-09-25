package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Renameable;

/**
 * Created by tomc on 30/01/15.
 */
public interface ProxyRenameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> extends Renameable<RENAME_COMMAND> {

}
