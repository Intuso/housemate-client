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

/**
 * @param <ROOT> the type of the root
 */
public abstract class ProxyRoot<
        SERVER extends ProxyServer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
        SERVERS extends ProxyList<?, SERVER, SERVERS>,
        ROOT extends ProxyRoot<SERVER, SERVERS, ROOT>>
        extends ProxyObject<RootData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, ROOT, ProxyRoot.Listener<? super ROOT>>
        implements Root<ProxyRoot.Listener<? super ROOT>, ROOT>,
        RequiresAccess,
        Message.Sender,
        ObjectRoot<ProxyRoot.Listener<? super ROOT>, ROOT>,
        Server.Container<SERVERS>,
        ObjectListener<ProxyObject<?, ?, ?, ?, ?>> {

    public final static String SERVERS_ID = "servers";

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    private final Router.Registration routerRegistration;
    private final AccessManager accessManager;
    private final MessageSequencer messageSequencer = new MessageSequencer(new Message.Receiver<Message.Payload>() {
        @Override
        public void messageReceived(Message<Message.Payload> message) {
            distributeMessage(message);
        }
    });

    /**
     * @param log {@inheritDoc}
     * @param router The router to connect through
     */
    public ProxyRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router<?> router) {
        super(log, listenersFactory, new RootData());
        accessManager = new AccessManager(listenersFactory, properties, ApplicationRegistration.ClientType.Proxy, this);
        init(null);
        routerRegistration = router.registerReceiver(new Router.Receiver() {
            @Override
            public void messageReceived(Message message) {
                if(message.getSequenceId() != null)
                    sendMessage(new Message<Message.Payload>(new String[] {""}, Message.RECEIVED_TYPE, new Message.ReceivedPayload(message.getSequenceId())));
                messageSequencer.messageReceived(message);
            }

            @Override
            public void serverConnectionStatusChanged(ClientConnection clientConnection, ServerConnectionStatus serverConnectionStatus) {

            }

            @Override
            public void newServerInstance(ClientConnection clientConnection, String serverId) {

            }
        });
    }

    @Override
    public Application.Status getApplicationStatus() {
        return accessManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return accessManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        accessManager.register(applicationDetails, component);
    }

    @Override
    public void unregister() {
        accessManager.unregister();
        routerRegistration.unregister();
    }

    @Override
    public void sendMessage(Message<?> message) {
        // if we're not allowed to send messages, and it's not a registration message, then throw an exception
        if(getApplicationInstanceStatus() != ApplicationInstance.Status.Allowed
                && !(message.getPath().length == 1 &&
                (message.getType().equals(ApplicationRegistration.APPLICATION_REGISTRATION_TYPE)
                        || message.getType().equals(ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE)
                        || message.getType().equals(Message.RECEIVED_TYPE))))
            throw new HousemateCommsException("Client application instance is not allowed access to the server");
        routerRegistration.sendMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        result.add(accessManager.addStatusChangeListener(new RequiresAccess.Listener<AccessManager>() {

            @Override
            public void applicationStatusChanged(AccessManager accessManager, Application.Status applicationStatus) {
                for (ProxyRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.applicationStatusChanged(getThis(), applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(AccessManager accessManager, ApplicationInstance.Status applicationInstanceStatus) {
                for (ProxyRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(getThis(), applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(AccessManager accessManager, String instanceId) {
                for (ProxyRoot.Listener<? super ROOT> listener : getObjectListeners())
                    listener.newApplicationInstance(getThis(), instanceId);
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                accessManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                accessManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                accessManager.setApplicationInstanceStatus(message.getPayload().getStatus());
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

    public interface Listener<ROOT extends ProxyRoot<?, ?, ?>> extends Root.Listener<ROOT>, RequiresAccess.Listener<ROOT> {}
}
