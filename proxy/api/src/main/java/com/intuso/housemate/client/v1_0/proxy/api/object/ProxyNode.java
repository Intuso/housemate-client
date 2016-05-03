package com.intuso.housemate.client.v1_0.proxy.api.object;

import com.intuso.housemate.client.v1_0.api.object.Node;
import com.intuso.housemate.client.v1_0.proxy.api.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <COMMAND> the type of the command
 * @param <HARDWARES> the type of the hardwares list
 * @param <NODE> the type of the node
 */
public abstract class ProxyNode<
        COMMAND extends ProxyCommand<?, ?, ?>,
        HARDWARES extends ProxyList<? extends ProxyHardware<?, ?, ?, ?, ?>>,
        NODE extends ProxyNode<COMMAND, HARDWARES, NODE>>
        extends ProxyObject<Node.Data, Node.Listener<? super NODE>>
        implements Node<COMMAND, HARDWARES, NODE> {

    private final HARDWARES hardwares;
    private final COMMAND addHardwareCommand;

    public ProxyNode(Logger logger,
                     ListenersFactory listenersFactory,
                     Factory<COMMAND> commandFactory,
                     Factory<HARDWARES> hardwaresFactory) {
        super(logger, Node.Data.class, listenersFactory);
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, Node.HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, Node.ADD_HARDWARE_ID));
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        hardwares.init(ChildUtil.name(name, Node.HARDWARES_ID), session);
        addHardwareCommand.init(ChildUtil.name(name, Node.ADD_HARDWARE_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        hardwares.uninit();
        addHardwareCommand.uninit();
    }

    @Override
    public HARDWARES getHardwares() {
        return hardwares;
    }

    @Override
    public COMMAND getAddHardwareCommand() {
        return addHardwareCommand;
    }
}
