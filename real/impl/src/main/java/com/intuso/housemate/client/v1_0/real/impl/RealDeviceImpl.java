package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;
import com.intuso.housemate.client.v1_0.api.object.view.DeviceView;
import com.intuso.housemate.client.v1_0.api.object.view.ListView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.impl.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public abstract class RealDeviceImpl<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        VIEW extends DeviceView<?>,
        DEVICE extends RealDeviceImpl<DATA, LISTENER, VIEW, DEVICE>>
        extends RealObject<DATA, LISTENER, VIEW>
        implements RealDevice<DATA, LISTENER, RealCommandImpl, RealListGeneratedImpl<RealCommandImpl>, RealListGeneratedImpl<RealValueImpl<?>>, VIEW, DEVICE> {

    private final RealCommandImpl renameCommand;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;

    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          DATA data,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory parameterFactory,
                          RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                          TypeRepository typeRepository) {
        super(logger, data, managedCollectionFactory, senderFactory);
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommand.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealDeviceImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID),
                COMMANDS_ID,
                "Commands",
                "The commands of this feature",
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID),
                VALUES_ID,
                "Values",
                "The values of this feature",
                Lists.<RealValueImpl<?>>newArrayList());
    }

    @Override
    public Tree getTree(VIEW view) {

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS)));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand()));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands()));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues()));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand()));
                    if(view.getCommands() != null)
                        result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands()));
                    if(view.getValues() != null)
                        result.getChildren().put(VALUES_ID, values.getTree(view.getValues()));
                    break;
            }
        }

        return result;
    }

    private void setName(String newName) {
        getData().setName(newName);
        for(Device.Listener<? super DEVICE> listener : listeners)
            listener.renamed(getThis(), RealDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID));
        commands.init(ChildUtil.name(name, COMMANDS_ID));
        values.init(ChildUtil.name(name, VALUES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    void clear() {
        for(RealCommandImpl command : Lists.newArrayList(commands))
            commands.remove(command.getId());
        for(RealValueImpl<?> value : Lists.newArrayList(values))
            values.remove(value.getId());
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }

    public DEVICE getThis() {
        return (DEVICE) this;
    }
}
