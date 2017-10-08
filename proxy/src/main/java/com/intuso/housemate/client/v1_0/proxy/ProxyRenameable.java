package com.intuso.housemate.client.v1_0.proxy;

import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.proxy.object.view.CommandView;

/**
 * Created by tomc on 30/01/15.
 */
public interface ProxyRenameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> extends Renameable<RENAME_COMMAND> {
    void viewRenameCommand(CommandView commandView);
}
