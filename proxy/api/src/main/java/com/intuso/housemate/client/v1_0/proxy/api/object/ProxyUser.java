package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.Removeable;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.User;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRemoveable;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <COMMAND> the type of the command
 * @param <USER> the type of the user
 */
public abstract class ProxyUser<
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            USER extends ProxyUser<COMMAND, PROPERTY, USER>>
        extends ProxyObject<User.Data, User.Listener<? super USER>>
        implements User<COMMAND, COMMAND, PROPERTY, USER>,
        ProxyRemoveable<COMMAND> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final PROPERTY emailProperty;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyUser(Logger logger,
                     ListenersFactory listenersFactory,
                     ProxyObject.Factory<COMMAND> commandFactory,
                     ProxyObject.Factory<PROPERTY> propertyFactory) {
        super(logger, User.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        emailProperty = propertyFactory.create(ChildUtil.logger(logger, User.EMAIL_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        emailProperty.init(ChildUtil.name(name, User.EMAIL_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        emailProperty.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public PROPERTY getEmailProperty() {
        return emailProperty;
    }
}
