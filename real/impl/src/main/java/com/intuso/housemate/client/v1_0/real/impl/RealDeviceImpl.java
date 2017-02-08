package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.impl.annotation.AnnotationParser;
import com.intuso.housemate.client.v1_0.real.impl.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealDeviceImpl
        extends RealObject<Device.Data, Device.Listener<? super RealDeviceImpl>>
        implements RealDevice<RealCommandImpl,
        RealListGeneratedImpl<RealCommandImpl>,
        RealListGeneratedImpl<RealValueImpl<?>>,
        RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealDeviceImpl> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;

    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          ManagedCollectionFactory managedCollectionFactory,
                          AnnotationParser annotationParser,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory parameterFactory,
                          RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                          RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                          TypeRepository typeRepository) {
        super(logger, new Device.Data(id, name, description), managedCollectionFactory);
        this.annotationParser = annotationParser;
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
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID),
                PROPERTIES_ID,
                PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
    }

    private void setName(String newName) {
        getData().setName(newName);
        for(Device.Listener<? super RealDeviceImpl> listener : listeners)
            listener.renamed(RealDeviceImpl.this, RealDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        commands.init(ChildUtil.name(name, COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, VALUES_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
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

    @Override
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    void clear() {
        for(RealCommandImpl command : Lists.newArrayList(commands))
            commands.remove(command.getId());
        for(RealValueImpl<?> value : Lists.newArrayList(values))
            values.remove(value.getId());
        for(RealPropertyImpl<?> property : Lists.newArrayList(properties))
            properties.remove(property.getId());
    }

    void wrap(Object object) {
        clear();
        // add the commands, values and properties specified by the object
        for(RealCommandImpl command : annotationParser.findCommands(ChildUtil.logger(logger, COMMANDS_ID), "", object))
            commands.add(command);
        for(RealValueImpl<?> value : annotationParser.findValues(ChildUtil.logger(logger, VALUES_ID), "", object))
            values.add(value);
        for(RealPropertyImpl<?> property : annotationParser.findProperties(ChildUtil.logger(logger, PROPERTIES_ID), "", object))
            properties.add(property);
    }

    public interface Factory {
        RealDeviceImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Device.Data, RealDeviceImpl> {

        private final RealDeviceImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealDeviceImpl create(Logger logger, Device.Data data, RealListPersistedImpl.RemoveCallback<RealDeviceImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription());
        }
    }
}
