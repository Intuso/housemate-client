package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.real.api.RealServer;
import com.intuso.housemate.client.v1_0.real.impl.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.user.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public final class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealAutomationImpl, RealListImpl<RealAutomationImpl>,
        RealDeviceImpl<?>, RealListImpl<RealDeviceImpl<?>>,
        RealUserImpl, RealListImpl<RealUserImpl>,
        RealNodeImpl, RealListImpl<RealNodeImpl>,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceCommand.Callback,
        AddUserCommand.Callback {

    private final RealListImpl<RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListImpl<RealDeviceImpl<?>> devices;
    private final RealCommandImpl addDeviceCommand;
    private final RealListImpl<RealUserImpl> users;
    private final RealCommandImpl addUserCommand;
    private final RealListImpl<RealNodeImpl> nodes;

    private RealServerImpl(Logger logger,
                           Server.Data data,
                           ListenersFactory listenersFactory,
                           AddAutomationCommand.Factory addAutomationCommandFactory,
                           AddDeviceCommand.Factory addDeviceCommandFactory,
                           AddUserCommand.Factory addUserCommandFactory) {
        super(logger, data, listenersFactory);
        this.automations = new RealListImpl<>(ChildUtil.logger(logger, AUTOMATIONS_ID), new List.Data(AUTOMATIONS_ID, "Automations", "Automations"), listenersFactory);
        this.addAutomationCommand = addAutomationCommandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                new Command.Data(ADD_AUTOMATION_ID, ADD_AUTOMATION_ID, "Add automation"),
                this,
                this);
        this.devices = new RealListImpl<>(ChildUtil.logger(logger, DEVICES_ID), new List.Data(DEVICES_ID, "Devices", "Devices"), listenersFactory);
        this.addDeviceCommand = addDeviceCommandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_ID),
                new Command.Data(ADD_DEVICE_ID, ADD_DEVICE_ID, "Add device"),
                this,
                this);
        this.users = new RealListImpl<>(ChildUtil.logger(logger, USERS_ID), new List.Data(USERS_ID, "Users", "Users"), listenersFactory);
        this.addUserCommand = addUserCommandFactory.create(ChildUtil.logger(logger, ADD_USER_ID),
                new Command.Data(ADD_USER_ID, ADD_USER_ID, "Add user"),
                this,
                this);
        this.nodes = new RealListImpl<>(ChildUtil.logger(logger, NODES_ID), new List.Data(NODES_ID, "Nodes", "Nodes"), listenersFactory);
    }

    @Inject
    public RealServerImpl(ListenersFactory listenersFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          Connection connection) throws JMSException {
        this(LoggerFactory.getLogger("com.intuso.housemate.server"), new Server.Data("server", "server", "server"),
                listenersFactory, addAutomationCommandFactory, addDeviceCommandFactory, addUserCommandFactory);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.automations.init(AUTOMATIONS_ID, session);
        this.addAutomationCommand.init(ADD_AUTOMATION_ID, session);
        this.devices.init(DEVICES_ID, session);
        this.addDeviceCommand.init(ADD_DEVICE_ID, session);
        this.users.init(USERS_ID, session);
        this.addUserCommand.init(ADD_USER_ID, session);
        this.nodes.init(NODES_ID, session);
    }

    @Override
    public RealListImpl<RealAutomationImpl> getAutomations() {
        return automations;
    }

    @Override
    public RealCommandImpl getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public final void addAutomation(RealAutomationImpl automation) {
        automations.add(automation);
    }

    @Override
    public final void removeAutomation(RealAutomationImpl realAutomation) {
        automations.remove(realAutomation.getId());
    }

    @Override
    public RealListImpl<RealDeviceImpl<?>> getDevices() {
        return devices;
    }

    @Override
    public RealCommandImpl getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void addDevice(RealDeviceImpl<?> device) {
        devices.add(device);
    }

    @Override
    public void removeDevice(RealDeviceImpl<?> device) {
        devices.remove(device.getId());
    }

    @Override
    public RealListImpl<RealUserImpl> getUsers() {
        return users;
    }

    @Override
    public RealCommandImpl getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public void addUser(RealUserImpl user) {
        users.add(user);
    }

    @Override
    public void removeUser(RealUserImpl user) {
        users.remove(user.getId());
    }

    @Override
    public RealListImpl<RealNodeImpl> getNodes() {
        return nodes;
    }

    @Override
    public void addNode(RealNodeImpl node) {
        nodes.add(node);
    }

    @Override
    public void removeNode(RealNodeImpl node) {
        nodes.remove(node.getId());
    }
}