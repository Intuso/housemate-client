package com.intuso.housemate.client.v1_0.proxy;

import com.intuso.housemate.client.v1_0.api.Runnable;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;

/**
 * Classes implementing this are runnable objects
 * @param <START_STOP_COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the value
 */
public interface ProxyRunnable<START_STOP_COMMAND extends Command<?, ?, ?, ?>, RUNNING_VALUE extends Value<?, ?, ?>> extends Runnable<START_STOP_COMMAND, RUNNING_VALUE> {
    void loadStartCommand(CommandView commandView);
    void loadStopCommand(CommandView commandView);
    boolean isRunning();
}