package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Task;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.view.*;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.*;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public abstract class ProxyTask<
        COMMAND extends ProxyCommand<?, ?, ?>,
        VALUE extends ProxyValue<?, ?>,
        PROPERTY extends ProxyProperty<?, ?, ?>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        TASK extends ProxyTask<COMMAND, VALUE, PROPERTY, PROPERTIES, TASK>>
        extends ProxyObject<Task.Data, Task.Listener<? super TASK>, TaskView>
        implements Task<COMMAND, COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, TASK>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<VALUE> valueFactory;
    private final ProxyObject.Factory<PROPERTY> propertyFactory;
    private final ProxyObject.Factory<PROPERTIES> propertiesFactory;

    private COMMAND renameCommand;
    private COMMAND removeCommand;
    private VALUE errorValue;
    private PROPERTY driverProperty;
    private VALUE driverLoadedValue;
    private PROPERTIES properties;
    private VALUE executingValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyTask(Logger logger,
                     String path,
                     String name,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory,
                     ProxyObject.Factory<COMMAND> commandFactory,
                     ProxyObject.Factory<VALUE> valueFactory,
                     ProxyObject.Factory<PROPERTY> propertyFactory,
                     ProxyObject.Factory<PROPERTIES> propertiesFactory) {
        super(logger, path, name, Task.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.valueFactory = valueFactory;
        this.propertyFactory = propertyFactory;
        this.propertiesFactory = propertiesFactory;
    }

    @Override
    public TaskView createView(View.Mode mode) {
        return new TaskView(mode);
    }

    @Override
    public Tree getTree(TaskView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(new PropertyView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(EXECUTING_ID, executingValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(EXECUTING_ID, executingValue.getTree(view.getExecutingValue(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDriverProperty() != null)
                        result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDriverLoadedValue() != null)
                        result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), referenceHandler, listener, listenerRegistrations));
                    if(view.getProperties() != null)
                        result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), referenceHandler, listener, listenerRegistrations));
                    if(view.getExecutingValue() != null)
                        result.getChildren().put(EXECUTING_ID, executingValue.getTree(view.getExecutingValue(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(TaskView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.path(path, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.path(path, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(properties == null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.path(path, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(executingValue == null)
                    executingValue = valueFactory.create(ChildUtil.logger(logger, EXECUTING_ID), ChildUtil.path(path, EXECUTING_ID), ChildUtil.name(name, EXECUTING_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommand() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommand() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(errorValue == null && view.getErrorValue() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(driverProperty == null && view.getDriverProperty() != null)
                    driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.path(path, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
                if(driverLoadedValue == null && view.getDriverLoadedValue() != null)
                    driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.path(path, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
                if(properties == null && view.getProperties() != null)
                    properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.path(path, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
                if(executingValue == null && view.getExecutingValue() != null)
                    executingValue = valueFactory.create(ChildUtil.logger(logger, EXECUTING_ID), ChildUtil.path(path, EXECUTING_ID), ChildUtil.name(name, EXECUTING_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                removeCommand.load(new CommandView(View.Mode.ANCESTORS));
                errorValue.load(new ValueView(View.Mode.ANCESTORS));
                driverProperty.load(new PropertyView(View.Mode.ANCESTORS));
                driverLoadedValue.load(new ValueView(View.Mode.ANCESTORS));
                properties.load(new ListView(View.Mode.ANCESTORS));
                executingValue.load(new ValueView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommand() != null)
                    renameCommand.load(view.getRenameCommand());
                if(view.getRemoveCommand() != null)
                    removeCommand.load(view.getRemoveCommand());
                if(view.getErrorValue() != null)
                    errorValue.load(view.getErrorValue());
                if(view.getDriverProperty() != null)
                    driverProperty.load(view.getDriverProperty());
                if(view.getDriverLoadedValue() != null)
                    driverLoadedValue.load(view.getDriverLoadedValue());
                if(view.getProperties() != null)
                    properties.load(view.getProperties());
                if(view.getExecutingValue() != null)
                    executingValue.load(view.getExecutingValue());
                break;
        }
    }

    @Override
    public void loadRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.load(commandView);
    }

    @Override
    public void loadRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.load(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(removeCommand != null)
            removeCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        if(driverProperty != null)
            driverProperty.uninit();
        if(driverLoadedValue != null)
            driverLoadedValue.uninit();
        if(properties != null)
            properties.uninit();
        if(executingValue != null)
            executingValue.uninit();
    }

    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValues() != null ? errorValue.getValues().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValues() != null
                && driverLoadedValue.getValues().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValues().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    public final boolean isExecuting() {
        VALUE executing = getExecutingValue();
        return executing.getValues() != null
                && executing.getValues().getFirstValue() != null
                && Boolean.parseBoolean(executing.getValues().getFirstValue());
    }

    @Override
    public final VALUE getExecutingValue() {
        return executingValue;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        } else if(ERROR_ID.equals(id)) {
            if(errorValue == null)
                errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
            return errorValue;
        } else if(DRIVER_ID.equals(id)) {
            if(driverProperty == null)
                driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID), ChildUtil.path(path, DRIVER_ID), ChildUtil.name(name, DRIVER_ID));
            return driverProperty;
        } else if(DRIVER_LOADED_ID.equals(id)) {
            if(driverLoadedValue == null)
                driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID), ChildUtil.path(path, DRIVER_LOADED_ID), ChildUtil.name(name, DRIVER_LOADED_ID));
            return driverLoadedValue;
        } else if(PROPERTIES_ID.equals(id)) {
            if(properties == null)
                properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID), ChildUtil.path(path, PROPERTIES_ID), ChildUtil.name(name, PROPERTIES_ID));
            return properties;
        } else if(EXECUTING_ID.equals(id)) {
            if(executingValue == null)
                executingValue = valueFactory.create(ChildUtil.logger(logger, EXECUTING_ID), ChildUtil.path(path, EXECUTING_ID), ChildUtil.name(name, EXECUTING_ID));
            return executingValue;
        }
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyTask<
            ProxyCommand.Simple,
            ProxyValue.Simple,
            ProxyProperty.Simple,
            ProxyList.Simple<ProxyProperty.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyProperty.Simple> propertyFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory);
        }
    }
}
