package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.real.api.RealServer;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Root;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddAutomationCommand;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealServerImpl
        extends RealObject<Server.Data, Server.Listener<? super RealServerImpl>>
        implements RealServer<RealCommandImpl,
        RealAutomationImpl, RealListPersistedImpl<RealAutomationImpl>,
        RealDeviceImpl, RealListPersistedImpl<RealDeviceImpl>,
        RealUserImpl, RealListPersistedImpl<RealUserImpl>,
        RealNodeImpl, RealListGeneratedImpl<RealNodeImpl>,
        RealServerImpl>,
        AddAutomationCommand.Callback,
        AddDeviceCommand.Callback,
        AddUserCommand.Callback {

    private final RealListPersistedImpl<RealAutomationImpl> automations;
    private final RealCommandImpl addAutomationCommand;
    private final RealListPersistedImpl<RealDeviceImpl> devices;
    private final RealCommandImpl addDeviceCommand;
    private final RealListGeneratedImpl<RealNodeImpl> nodes;
    private final RealListPersistedImpl<RealUserImpl> users;
    private final RealCommandImpl addUserCommand;

    @AssistedInject
    private RealServerImpl(@Assisted Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           ListenersFactory listenersFactory,
                           final RealAutomationImpl.Factory automationFactory,
                           RealListPersistedImpl.Factory<RealAutomationImpl> automationsFactory,
                           final RealDeviceImpl.Factory deviceFactory,
                           RealListPersistedImpl.Factory<RealDeviceImpl> devicesFactory,
                           RealListGeneratedImpl.Factory<RealNodeImpl> nodesFactory,
                           final RealUserImpl.Factory userFactory,
                           RealListPersistedImpl.Factory<RealUserImpl> usersFactory,
                           AddAutomationCommand.Factory addAutomationCommandFactory,
                           AddDeviceCommand.Factory addDeviceCommandFactory,
                           AddUserCommand.Factory addUserCommandFactory) {
        super(logger, new Server.Data(id, name, description), listenersFactory);
        this.automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
                AUTOMATIONS_ID,
                "Automations",
                "Automations",
                new RealListPersistedImpl.ExistingObjectFactory<RealAutomationImpl>() {
                    @Override
                    public RealAutomationImpl create(Logger parentLogger, Object.Data data) {
                        return automationFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addAutomationCommand = addAutomationCommandFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID),
                ChildUtil.logger(logger, ADD_AUTOMATION_ID),
                ADD_AUTOMATION_ID,
                ADD_AUTOMATION_ID,
                "Add automation",
                this,
                this);
        this.devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID),
                DEVICES_ID,
                "Devices",
                "Devices",
                new RealListPersistedImpl.ExistingObjectFactory<RealDeviceImpl>() {
                    @Override
                    public RealDeviceImpl create(Logger parentLogger, Object.Data data) {
                        return deviceFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addDeviceCommand = addDeviceCommandFactory.create(ChildUtil.logger(logger, DEVICES_ID),
                ChildUtil.logger(logger, ADD_DEVICE_ID),
                ADD_DEVICE_ID,
                ADD_DEVICE_ID,
                "Add device",
                this,
                this);
        this.users = usersFactory.create(ChildUtil.logger(logger, USERS_ID),
                USERS_ID,
                "Users",
                "Users",
                new RealListPersistedImpl.ExistingObjectFactory<RealUserImpl>() {
                    @Override
                    public RealUserImpl create(Logger parentLogger, Object.Data data) {
                        return userFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealServerImpl.this);
                    }
                });
        this.addUserCommand = addUserCommandFactory.create(ChildUtil.logger(logger, USERS_ID),
                ChildUtil.logger(logger, ADD_USER_ID),
                ADD_USER_ID,
                ADD_USER_ID,
                "Add user",
                this,
                this);
        this.nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID),
                NODES_ID,
                "Nodes",
                "Nodes",
                Lists.<RealNodeImpl>newArrayList());
    }

    @Inject
    public RealServerImpl(@Root Logger logger,
                          ListenersFactory listenersFactory,
                          RealAutomationImpl.Factory automationFactory,
                          RealListPersistedImpl.Factory<RealAutomationImpl> automationsFactory,
                          RealDeviceImpl.Factory deviceFactory,
                          RealListPersistedImpl.Factory<RealDeviceImpl> devicesFactory,
                          RealListGeneratedImpl.Factory<RealNodeImpl> nodesFactory,
                          RealUserImpl.Factory userFactory,
                          RealListPersistedImpl.Factory<RealUserImpl> usersFactory,
                          RealNodeImpl.Factory nodeFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          Connection connection) throws JMSException {
        this(logger, "server", "server", "server", listenersFactory, automationFactory, automationsFactory,
                deviceFactory, devicesFactory, nodesFactory, userFactory, usersFactory, addAutomationCommandFactory,
                addDeviceCommandFactory, addUserCommandFactory);
        this.automations.init(AUTOMATIONS_ID, connection);
        this.addAutomationCommand.init(ADD_AUTOMATION_ID, connection);
        this.devices.init(DEVICES_ID, connection);
        this.addDeviceCommand.init(ADD_DEVICE_ID, connection);
        this.users.init(USERS_ID, connection);
        this.addUserCommand.init(ADD_USER_ID, connection);
        this.nodes.init(NODES_ID, connection);
        this.nodes.add(nodeFactory.create(ChildUtil.logger(logger, NODES_ID, "local"), "local", "Local", "Local Node"));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.automations.init(AUTOMATIONS_ID, connection);
        this.addAutomationCommand.init(ADD_AUTOMATION_ID, connection);
        this.devices.init(DEVICES_ID, connection);
        this.addDeviceCommand.init(ADD_DEVICE_ID, connection);
        this.users.init(USERS_ID, connection);
        this.addUserCommand.init(ADD_USER_ID, connection);
        this.nodes.init(NODES_ID, connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.automations.uninit();
        this.addAutomationCommand.uninit();
        this.devices.uninit();
        this.addDeviceCommand.uninit();
        this.users.uninit();
        this.addUserCommand.uninit();
        this.nodes.uninit();
    }

    @Override
    public RealListPersistedImpl<RealAutomationImpl> getAutomations() {
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
    public RealListPersistedImpl<RealDeviceImpl> getDevices() {
        return devices;
    }

    @Override
    public RealCommandImpl getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void addDevice(RealDeviceImpl device) {
        devices.add(device);
    }

    @Override
    public void removeDevice(RealDeviceImpl device) {
        devices.remove(device.getId());
    }

    @Override
    public RealListPersistedImpl<RealUserImpl> getUsers() {
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
    public RealListGeneratedImpl<RealNodeImpl> getNodes() {
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

    public interface Factory {
        RealServerImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description);
    }
}