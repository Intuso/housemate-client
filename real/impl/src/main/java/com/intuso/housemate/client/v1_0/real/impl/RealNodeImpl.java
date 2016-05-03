package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Node;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public final class RealNodeImpl
        extends RealObject<Node.Data, Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealHardwareImpl<?>, RealListImpl<RealHardwareImpl<?>>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final RealListImpl<RealHardwareImpl<?>> hardwares;
    private final RealCommandImpl addHardwareCommand;

    private RealNodeImpl(Logger logger,
                         Node.Data data,
                         ListenersFactory listenersFactory,
                         AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, data, listenersFactory);
        this.hardwares = new RealListImpl<>(ChildUtil.logger(logger, HARDWARES_ID), new com.intuso.housemate.client.v1_0.api.object.List.Data(HARDWARES_ID, "Hardware", "Hardware"), listenersFactory);
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(ChildUtil.logger(logger, ADD_HARDWARE_ID), ADD_HARDWARE_ID), ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware", this, this);
    }

    public RealNodeImpl(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, AddHardwareCommand.Factory addHardwareCommandFactory) {
        this(logger, new Node.Data(id, name, description), listenersFactory, addHardwareCommandFactory);
    }

    @Inject
    public RealNodeImpl(ListenersFactory listenersFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        Connection connection) throws JMSException {
        this(LoggerFactory.getLogger("com.intuso.housemate.node"), new Node.Data("node", "node", "node"), listenersFactory, addHardwareCommandFactory);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.hardwares.init(HARDWARES_ID, session);
        this.addHardwareCommand.init(ADD_HARDWARE_ID, session);
    }

    @Override
    public RealListImpl<RealHardwareImpl<?>> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl<?> hardware) {
        hardwares.add(hardware);
    }

    @Override
    public final void removeHardware(RealHardwareImpl<?> realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
        return addHardwareCommand;
    }
}