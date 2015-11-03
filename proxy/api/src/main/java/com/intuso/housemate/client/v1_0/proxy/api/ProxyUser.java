package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;
import com.intuso.housemate.object.v1_0.api.User;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <COMMAND> the type of the command
 * @param <USER> the type of the user
 */
public abstract class ProxyUser<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            USER extends ProxyUser<COMMAND, PROPERTY, USER>>
        extends ProxyObject<UserData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, USER, User.Listener<? super USER>>
        implements User<COMMAND, PROPERTY, USER>,
        ProxyRemoveable<COMMAND> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyUser(Log log, ListenersFactory listenersFactory, UserData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(UserData.REMOVE_ID);
    }

    @Override
    public PROPERTY getEmailProperty() {
        return (PROPERTY) getChild(UserData.EMAIL_ID);
    }
}
