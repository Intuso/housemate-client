package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.comms.v1_0.api.*;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationRegistration;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.*;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <ROOT> the type of the root
 */
public abstract class ProxyRoot<
        SERVER extends ProxyServer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
        SERVERS extends ProxyList<?, SERVER, SERVERS>,
        ROOT extends ProxyRoot<SERVER, SERVERS, ROOT>>
        extends ProxyObject<RootData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, ROOT, ClientRoot.Listener<? super ROOT>>
        implements ClientRoot<ClientRoot.Listener<? super ROOT>, ROOT>,
        ObjectRoot<ClientRoot.Listener<? super ROOT>, ROOT>,
        Server.Container<SERVERS>,
        ObjectListener<ProxyObject<?, ?, ?, ?, ?>> {

    public final static String SERVERS_ID = "servers";

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    /**
     * @param log {@inheritDoc}
     * @param router The router to connect through
     */
    public ProxyRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        super(log, listenersFactory, new RootData());
        connectionManager = new ConnectionManager(listenersFactory, properties, ApplicationRegistration.ClientType.Proxy, this);
        init(null);
        routerRegistration = router.registerReceiver(this);
    }

    @Override
    public Application.Status getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        connectionManager.register(applicationDetails, component);
    }

    @Override
    public void unregister() {
        connectionManager.unregister();
        routerRegistration.unregister();
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        // if we're not allowed to send messages, and it's not a registration message, then throw an exception
        if(getApplicationInstanceStatus() != ApplicationInstance.Status.Allowed
                && !(message.getPath().length == 1 &&
                (message.getType().equals(ApplicationRegistration.APPLICATION_REGISTRATION_TYPE)
                        || message.getType().equals(ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE))))
            throw new HousemateCommsException("Client application instance is not allowed access to the server");
        routerRegistration.sendMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        result.add(connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for (ClientRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(getThis(), serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(Application.Status applicationStatus) {
                for (ClientRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.applicationStatusChanged(getThis(), applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstance.Status applicationInstanceStatus) {
                for (ClientRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(getThis(), applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for (ClientRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.newApplicationInstance(getThis(), instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                Set<String> ids = Sets.newHashSet();
                for (RemoteObject<?, ?, ?, ?> child : getChildren()) {
                    child.uninit();
                    ids.add(child.getId());
                }
                for (String id : ids)
                    removeChild(id);
                for (ClientRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.newServerInstance(getThis(), serverId);
            }
        }));
        result.add(addMessageListener(RootData.SERVER_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.SERVER_CONNECTION_STATUS_TYPE, new Message.Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                connectionManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                connectionManager.setApplicationInstanceStatus(message.getPayload().getStatus());
            }
        }));
        return result;
    }

    @Override
    public SERVERS getServers() {
        return (SERVERS) getChild(SERVERS_ID);
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
