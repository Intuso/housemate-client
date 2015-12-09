package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.NoPayload;
import com.intuso.housemate.comms.v1_0.api.payload.ServerData;
import com.intuso.housemate.object.v1_0.api.ObjectLifecycleListener;
import com.intuso.housemate.object.v1_0.api.Server;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.Map;

/**
 * @param <USER> the type of the users
 * @param <USERS> the type of the users list
 * @param <TYPE> the type of the types
 * @param <TYPES> the type of the types list
 * @param <DEVICE> the type of the devices
 * @param <DEVICES> the type of the devices list
 * @param <AUTOMATION> the type of the automations
 * @param <AUTOMATIONS> the type of the automations list
 * @param <COMMAND> the type of the command
 * @param <SERVER> the type of the root
 */
public abstract class ProxyServer<
            APPLICATION extends ProxyApplication<?, ?, ?, ?, APPLICATION>,
            APPLICATIONS extends ProxyList<?, APPLICATION, APPLICATIONS>,
            AUTOMATION extends ProxyAutomation<?, ?, ?, ?, ?, ?, ?>,
            AUTOMATIONS extends ProxyList<?, AUTOMATION, AUTOMATIONS>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?>,
            DEVICES extends ProxyList<?, DEVICE, DEVICES>,
            HARDWARE extends ProxyHardware<?, ?, ?, ?, ?>,
            HARDWARES extends ProxyList<?, HARDWARE, HARDWARES>,
            TYPE extends ProxyType<?, ?, ?, ?>,
            TYPES extends ProxyList<?, TYPE, TYPES>,
            USER extends ProxyUser<?, ?, USER>,
            USERS extends ProxyList<?, USER, USERS>,
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            SERVER extends ProxyServer<APPLICATION, APPLICATIONS, AUTOMATION, AUTOMATIONS, DEVICE, DEVICES, HARDWARE, HARDWARES, TYPE, TYPES, USER, USERS, COMMAND, SERVER>>
        extends ProxyObject<ServerData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, SERVER, Server.Listener<? super SERVER>>
        implements Server<APPLICATIONS, AUTOMATIONS, DEVICES, HARDWARES, TYPES, USERS, COMMAND, SERVER>,
            ObjectListener<ProxyObject<?, ?, ?, ?, ?>>,
            ProxyRenameable<COMMAND> {

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    public ProxyServer(Logger logger, ListenersFactory listenersFactory, ServerData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    public APPLICATIONS getApplications() {
        return (APPLICATIONS) getChild(ServerData.APPLICATIONS_ID);
    }

    @Override
    public USERS getUsers() {
        return (USERS) getChild(ServerData.USERS_ID);
    }

    @Override
    public HARDWARES getHardwares() {
        return (HARDWARES) getChild(ServerData.HARDWARES_ID);
    }

    @Override
    public TYPES getTypes() {
        return (TYPES) getChild(ServerData.TYPES_ID);
    }

    @Override
    public DEVICES getDevices() {
        return (DEVICES) getChild(ServerData.DEVICES_ID);
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return (AUTOMATIONS) getChild(ServerData.AUTOMATIONS_ID);
    }

    public COMMAND getAddUserCommand() {
        return (COMMAND) getChild(ServerData.ADD_USER_ID);
    }

    public COMMAND getAddHardwareCommand() {
        return (COMMAND) getChild(ServerData.ADD_HARDWARE_ID);
    }

    public COMMAND getAddDeviceCommand() {
        return (COMMAND) getChild(ServerData.ADD_DEVICE_ID);
    }

    public COMMAND getAddAutomationCommand() {
        return (COMMAND) getChild(ServerData.ADD_AUTOMATION_ID);
    }

    @Override
    public COMMAND getRenameCommand() {
        return (COMMAND) getChild(ServerData.RENAME_ID);
    }

    @Override
    public void childObjectAdded(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectAdded(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectRemoved(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = getListenersFactory().create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    public void clearLoadedObjects() {
        sendMessage("clear-loaded", NoPayload.INSTANCE);
        // clone the set so we can edit it while we iterate it
        for(String childName : Sets.newHashSet(getChildNames()))
            removeChild(childName);
    }
}
