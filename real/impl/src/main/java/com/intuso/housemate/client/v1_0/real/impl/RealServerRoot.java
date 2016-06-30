package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddAutomationCommand;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealServerRoot extends RealServerImpl {

    private final Connection connection;
    private final RealNodeImpl.Factory nodeFactory;

    @Inject
    public RealServerRoot(@com.intuso.housemate.client.v1_0.real.impl.ioc.Server Logger logger,
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
        super(logger, "server", "server", "server", listenersFactory, automationFactory, automationsFactory,
                deviceFactory, devicesFactory, nodesFactory, userFactory, usersFactory, addAutomationCommandFactory,
                addDeviceCommandFactory, addUserCommandFactory);
        this.connection = connection;
        this.nodeFactory = nodeFactory;
    }

    public void start() {
        try {
            init("server", connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
        addNode(nodeFactory.create(ChildUtil.logger(logger, NODES_ID, "local"), "local", "Local", "Local Node"));
    }

    public void stop() {
        uninit();
    }

    public static class Service extends AbstractIdleService {

        private final RealServerRoot server;

        @Inject
        public Service(RealServerRoot server) {
            this.server = server;
        }

        @Override
        protected void startUp() throws Exception {
            server.start();
        }

        @Override
        protected void shutDown() throws Exception {
            server.stop();
        }
    }
}