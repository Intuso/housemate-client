package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.real.api.object.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Node;
import com.intuso.housemate.client.v1_0.real.impl.type.RegisteredTypes;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public class RealNodeImpl
        extends RealObject<com.intuso.housemate.client.v1_0.api.object.Node.Data, com.intuso.housemate.client.v1_0.api.object.Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealListGeneratedImpl<RealTypeImpl<?>>, RealHardwareImpl, RealListPersistedImpl<RealHardwareImpl>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final String id;
    private final Connection connection;

    private final RealListGeneratedImpl<RealTypeImpl<?>> types;
    private final RealListPersistedImpl<RealHardwareImpl> hardwares;
    private final RealCommandImpl addHardwareCommand;

    @Inject
    public RealNodeImpl(Connection connection,
                        @Node final Logger logger,
                        @Node String id,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        final RealHardwareImpl.Factory hardwareFactory,
                        RealListPersistedImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, true, new com.intuso.housemate.client.v1_0.api.object.Node.Data(id, "node", "node"), listenersFactory);
        this.id = id;
        this.connection = connection;
        this.types = registeredTypes.createList(ChildUtil.logger(logger, TYPES_ID),
                TYPES_ID,
                "Types",
                "Types");
        this.hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                HARDWARES_ID,
                "Hardware",
                "Hardware",
                new RealListPersistedImpl.ExistingObjectFactory<RealHardwareImpl>() {
                    @Override
                    public RealHardwareImpl create(Logger parentLogger, Object.Data data) {
                        return hardwareFactory.create(ChildUtil.logger(parentLogger, data.getId()), data.getId(), data.getName(), data.getDescription(), RealNodeImpl.this);
                    }
                });
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                ChildUtil.logger(logger, ADD_HARDWARE_ID),
                ADD_HARDWARE_ID,
                ADD_HARDWARE_ID,
                "Add hardware",
                this,
                this);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        types.init(ChildUtil.name(name, TYPES_ID), connection);
        hardwares.init(ChildUtil.name(name, HARDWARES_ID), connection);
        addHardwareCommand.init(ChildUtil.name(name, ADD_HARDWARE_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        types.uninit();
        hardwares.uninit();
        addHardwareCommand.uninit();
    }

    @Override
    public RealListGeneratedImpl<RealTypeImpl<?>> getTypes() {
        return types;
    }

    @Override
    public RealListPersistedImpl<RealHardwareImpl> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl hardware) {
        hardwares.add(hardware);
    }

    @Override
    public final void removeHardware(RealHardwareImpl realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
        return addHardwareCommand;
    }

    public void start() {
        try {
            init(ChildUtil.name(null, Object.VERSION, RealObject.REAL, Server.NODES_ID, id), connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
    }

    public void stop() {
        uninit();
    }

    public static class Service extends AbstractIdleService {

        private final RealNodeImpl node;

        @Inject
        public Service(RealNodeImpl node) {
            this.node = node;
        }

        @Override
        protected void startUp() throws Exception {
            node.start();
        }

        @Override
        protected void shutDown() throws Exception {
            node.stop();
        }
    }
}