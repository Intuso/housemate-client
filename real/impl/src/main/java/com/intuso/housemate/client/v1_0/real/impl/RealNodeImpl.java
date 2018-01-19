package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;
import com.intuso.housemate.client.v1_0.api.object.view.ListView;
import com.intuso.housemate.client.v1_0.api.object.view.NodeView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealNode;
import com.intuso.housemate.client.v1_0.real.impl.utils.AddHardwareCommand;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RealNodeImpl
        extends RealObject<com.intuso.housemate.client.v1_0.api.object.Node.Data, com.intuso.housemate.client.v1_0.api.object.Node.Listener<? super RealNodeImpl>, NodeView>
        implements RealNode<RealCommandImpl, RealListGeneratedImpl<RealTypeImpl<?>>, RealHardwareImpl, RealListPersistedImpl<Hardware.Data, RealHardwareImpl>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    public final static String NODE_ID = "node.id";
    public final static String NODE_NAME = "node.name";
    public final static String NODE_DESCRIPTION = "node.description";

    private final String id;

    private final RealListGeneratedImpl<RealTypeImpl<?>> types;
    private final RealListPersistedImpl<Hardware.Data, RealHardwareImpl> hardwares;
    private final RealCommandImpl addHardwareCommand;

    @Inject
    public RealNodeImpl(PropertyRepository propertyRepository,
                        ManagedCollectionFactory managedCollectionFactory,
                        Sender.Factory senderFactory,
                        RealListGeneratedImpl.Factory<RealTypeImpl<?>> typesFactory,
                        RealListPersistedImpl.Factory<Hardware.Data, RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(ChildUtil.logger(LoggerFactory.getLogger(RealObject.REAL), Object.VERSION, Server.NODES_ID, propertyRepository.get(NODE_ID)),
                new com.intuso.housemate.client.v1_0.api.object.Node.Data(propertyRepository.get(NODE_ID),
                        propertyRepository.get(NODE_NAME),
                        propertyRepository.get(NODE_DESCRIPTION)),
                managedCollectionFactory, senderFactory);
        this.id = propertyRepository.get(NODE_ID);
        this.types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID),
                TYPES_ID,
                "Types",
                "Types",
                Lists.<RealTypeImpl<?>>newArrayList());
        this.hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                HARDWARES_ID,
                "Hardware",
                "Hardware");
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                ChildUtil.logger(logger, ADD_HARDWARE_ID),
                ADD_HARDWARE_ID,
                ADD_HARDWARE_ID,
                "Add hardware",
                this,
                hardwares.getRemoveCallback());
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public NodeView createView(View.Mode mode) {
        return new NodeView(mode);
    }

    @Override
    public Tree getTree(NodeView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(TYPES_ID, types.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(HARDWARES_ID, hardwares.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(TYPES_ID, types.getTree(view.getTypes(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(HARDWARES_ID, hardwares.getTree(view.getHardwares(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(view.getAddHardwareCommand(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getTypes() != null)
                        result.getChildren().put(TYPES_ID, types.getTree(view.getTypes(), referenceHandler, listener, listenerRegistrations));
                    if(view.getHardwares() != null)
                        result.getChildren().put(HARDWARES_ID, hardwares.getTree(view.getHardwares(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddHardwareCommand() != null)
                        result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(view.getAddHardwareCommand(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        types.init(ChildUtil.name(name, TYPES_ID));
        hardwares.init(ChildUtil.name(name, HARDWARES_ID));
        addHardwareCommand.init(ChildUtil.name(name, ADD_HARDWARE_ID));
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
    public RealListPersistedImpl<Hardware.Data, RealHardwareImpl> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl hardware) {
        hardwares.add(hardware);
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id))
            return addHardwareCommand;
        else if(HARDWARES_ID.equals(id))
            return hardwares;
        else if(TYPES_ID.equals(id))
            return types;
        return null;
    }

    public void start() {
        init(ChildUtil.name(null, RealObject.REAL, Object.VERSION, Server.NODES_ID, id));
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