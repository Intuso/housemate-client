package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Node;
import com.intuso.housemate.client.v1_0.real.impl.type.RegisteredTypes;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddHardwareCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealNodeImpl
        extends RealObject<com.intuso.housemate.client.v1_0.api.object.Node.Data, com.intuso.housemate.client.v1_0.api.object.Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealListGeneratedImpl<RealTypeImpl<?>>, RealHardwareImpl, RealListPersistedImpl<RealHardwareImpl>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final RealListGeneratedImpl<RealTypeImpl<?>> types;
    private final RealListPersistedImpl<RealHardwareImpl> hardwares;
    private final RealCommandImpl addHardwareCommand;

    @AssistedInject
    public RealNodeImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        final RealHardwareImpl.Factory hardwareFactory,
                        RealListPersistedImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, true, new com.intuso.housemate.client.v1_0.api.object.Node.Data(id, name, description), listenersFactory);
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

    @Inject
    public RealNodeImpl(@Node Logger logger,
                        ListenersFactory listenersFactory,
                        RegisteredTypes registeredTypes,
                        RealHardwareImpl.Factory hardwareFactory,
                        RealListPersistedImpl.Factory<RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        Connection connection) throws JMSException {
        this(logger, "node", "node", "node", listenersFactory, registeredTypes, hardwareFactory, hardwaresFactory, addHardwareCommandFactory);
        this.types.init(TYPES_ID, connection);
        this.hardwares.init(HARDWARES_ID, connection);
        this.addHardwareCommand.init(ADD_HARDWARE_ID, connection);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        types.init(TYPES_ID, connection);
        hardwares.init(HARDWARES_ID, connection);
        addHardwareCommand.init(ADD_HARDWARE_ID, connection);
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

    public interface Factory {
        RealNodeImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description);
    }
}