package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRenameable;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <COMMAND> the type of the command
 * @param <AUTOMATIONS> the type of the automations list
 * @param <DEVICES> the type of the devices list
 * @param <USERS> the type of the users list
 * @param <NODES> the type of the nodes list
 * @param <SERVER> the type of the server
 */
public abstract class ProxyServer<
        COMMAND extends ProxyCommand<?, ?, ?>,
        AUTOMATIONS extends ProxyList<? extends ProxyAutomation<?, ?, ?, ?, ?>, ?>,
        DEVICES extends ProxyList<? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?>,
        USERS extends ProxyList<? extends ProxyUser<?, ?, ?>, ?>,
        NODES extends ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>,
        SERVER extends ProxyServer<COMMAND, AUTOMATIONS, DEVICES, USERS, NODES, SERVER>>
        extends ProxyObject<Server.Data, Server.Listener<? super SERVER>>
        implements Server<COMMAND, AUTOMATIONS, DEVICES, USERS, NODES, SERVER>,
            ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final AUTOMATIONS automations;
    private final COMMAND addAutomationCommand;
    private final DEVICES devices;
    private final COMMAND addDeviceCommand;
    private final USERS users;
    private final COMMAND addUserCommand;
    private final NODES nodes;

    public ProxyServer(Logger logger,
                       ListenersFactory listenersFactory,
                       ProxyObject.Factory<COMMAND> commandFactory,
                       ProxyObject.Factory<AUTOMATIONS> automationsFactory,
                       ProxyObject.Factory<DEVICES> devicesFactory,
                       ProxyObject.Factory<USERS> usersFactory,
                       ProxyObject.Factory<NODES> nodesFactory,
                       Connection connection) {
        super(logger, Server.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, Server.AUTOMATIONS_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_AUTOMATION_ID));
        devices = devicesFactory.create(ChildUtil.logger(logger, Server.DEVICES_ID));
        addDeviceCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_DEVICE_ID));
        users = usersFactory.create(ChildUtil.logger(logger, Server.USERS_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_USER_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, Server.NODES_ID));
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            renameCommand.init(Renameable.RENAME_ID, session);
            automations.init(Server.AUTOMATIONS_ID, session);
            addAutomationCommand.init(Server.ADD_AUTOMATION_ID, session);
            devices.init(Server.DEVICES_ID, session);
            addDeviceCommand.init(Server.ADD_DEVICE_ID, session);
            users.init(Server.USERS_ID, session);
            addUserCommand.init(Server.ADD_USER_ID, session);
            nodes.init(Server.NODES_ID, session);
        } catch(JMSException e) {
            throw new HousemateException("Failed to create session and initialise servers list");
        }
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        automations.init(ChildUtil.name(name, Server.AUTOMATIONS_ID), session);
        addAutomationCommand.init(ChildUtil.name(name, Server.ADD_AUTOMATION_ID), session);
        devices.init(ChildUtil.name(name, Server.DEVICES_ID), session);
        addDeviceCommand.init(ChildUtil.name(name, Server.ADD_DEVICE_ID), session);
        users.init(ChildUtil.name(name, Server.USERS_ID), session);
        addUserCommand.init(ChildUtil.name(name, Server.ADD_USER_ID), session);
        nodes.init(ChildUtil.name(name, Server.NODES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        automations.uninit();
        addAutomationCommand.uninit();
        devices.uninit();
        addDeviceCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return automations;
    }

    public COMMAND getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public DEVICES getDevices() {
        return devices;
    }

    public COMMAND getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public USERS getUsers() {
        return users;
    }

    public COMMAND getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public NODES getNodes() {
        return nodes;
    }
}
