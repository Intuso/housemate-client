package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.NoPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectFactory;
import com.intuso.utilities.object.ObjectListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <OBJECT> the type of the object
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends ProxyObject<DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends com.intuso.housemate.object.v1_0.api.ObjectListener>
        extends RemoteObject<DATA, CHILD_DATA, CHILD, LISTENER>
        implements ObjectListener<CHILD> {

    private ProxyRoot proxyRoot;
    private final Map<String, LoadManager> pendingLoads = Maps.newHashMap();
    private final Map<String, ChildOverview> childOverviews = Maps.newHashMap();
    private final Listeners<AvailableChildrenListener<? super OBJECT>> availableChildrenListeners;
    private final Map<String, Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>>> childLoadedListeners = Maps.newHashMap();

    private int nextLoaderId = 0;

    /**
     * @param log the log
     * @param listenersFactory
     * @param data the data object
     */
    protected ProxyObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(log, listenersFactory, data);
        availableChildrenListeners = listenersFactory.create();
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = Lists.newArrayList();
        result.add(addChildListener(this));
        result.add(addMessageListener(CHILD_OVERVIEWS_RESPONSE, new Message.Receiver<ChildOverviews>() {
            @Override
            public void messageReceived(Message<ChildOverviews> message) {
                for(ChildOverview childOverview : message.getPayload().getChildOverviews()) {
                    if(childOverviews.get(childOverview.getId()) == null) {
                        childOverviews.put(childOverview.getId(), childOverview);
                        for(AvailableChildrenListener<? super OBJECT> listener : availableChildrenListeners)
                            listener.childAdded(getThis(), childOverview);
                    }
                }
            }
        }));
        result.add(addMessageListener(CHILD_ADDED, new Message.Receiver<ChildOverview>() {
            @Override
            public void messageReceived(Message<ChildOverview> message) {
                ChildOverview childOverview = message.getPayload();
                if(childOverviews.get(childOverview.getId()) == null) {
                    childOverviews.put(childOverview.getId(), childOverview);
                    for(AvailableChildrenListener<? super OBJECT> listener : availableChildrenListeners)
                        listener.childAdded(getThis(), childOverview);
                }
            }
        }));
        result.add(addMessageListener(CHILD_REMOVED, new Message.Receiver<ChildOverview>() {
            @Override
            public void messageReceived(Message<ChildOverview> message) {
                ChildOverview childOverview = message.getPayload();
                childOverviews.remove(childOverview.getId());
                for (AvailableChildrenListener<? super OBJECT> listener : availableChildrenListeners)
                    listener.childRemoved(getThis(), childOverview);
            }
        }));
        result.add(addMessageListener(LOAD_RESPONSE, new Message.Receiver<TreeData>() {
            @Override
            public void messageReceived(Message<TreeData> message) {
                try {
                    if (message.getPayload().getData() != null) {
                        CHILD object = createChild(message.getPayload());
                        object.init(ProxyObject.this);
                        addChild(object);
                    }
                } catch (Throwable e) {
                    getLog().e("Failed to unwrap load response", e);
                }
            }
        }));
        result.add(addMessageListener(LOAD_FINISHED, new Message.Receiver<LoadFinished>() {
            @Override
            public void messageReceived(Message<LoadFinished> message) {
                LoadManager manager = pendingLoads.remove(message.getPayload().getLoaderId());
                if (manager != null) {
                    if (message.getPayload().getErrors() != null) {
                        getLog().e("Failed to load data for " + message.getPayload().getLoaderId() + " because " + Joiner.on(", ").join(message.getPayload().getErrors()));
                        manager.failed(message.getPayload().getErrors());
                    } else
                        manager.succeeded();
                }
            }
        }));
        return result;
    }

    protected final CHILD createChild(TreeData treeData) {
        CHILD child = createChildInstance((CHILD_DATA)treeData.getData());
        child.initObject(treeData);
        return child;
    }

    protected abstract CHILD createChildInstance(CHILD_DATA child_data);

    protected void initObject(TreeData treeData) {
        if(treeData.getChildOverviews() != null)
            childOverviews.putAll(treeData.getChildOverviews());
        for(TreeData childData : treeData.getChildren().values())
            addChild(createChild(childData));
    }

    @Override
    protected void initPreRecurseHook(RemoteObject<?, ?, ?, ?> parent) {

        // get the server for this object
        if(this instanceof ProxyRoot)
            proxyRoot = (ProxyRoot)this;
        else if(parent != null && parent instanceof ProxyObject)
            proxyRoot = ((ProxyObject)parent).proxyRoot;

        // unwrap children
        try {
            createChildren(new ObjectFactory<CHILD_DATA, CHILD>() {
                @Override
                public CHILD create(CHILD_DATA data) {
                    return createChildInstance(data);
                }
            });
        } catch(Throwable e) {
            throw new HousemateCommsException("Failed to unwrap child object", e);
        }
    }

    @Override
    protected void initPostRecurseHook(RemoteObject<?, ?, ?, ?> parent) {
        for(CHILD child : getChildren())
            if(!childOverviews.containsKey(child.getId()))
                childOverviews.put(child.getId(), new ChildOverview(child.getId(), child.getName(), child.getDescription()));
    }

    /**
     * Sends a message to the server
     * @param type the type of the message to send
     * @param payload the message payload
     * @param <MV> the type of the message payload
     */
    protected final <MV extends Message.Payload> void sendMessage(String type, MV payload) {
        proxyRoot.sendMessage(new Message<>(getPath(), type, payload));
    }

    /**
     * Gets the root object
     * @return the root object
     */
    protected ProxyRoot getProxyRoot() {
        return proxyRoot;
    }

    /**
     * Gets the names of the child objects
     * @return the names of the child objects
     */
    protected Set<String> getChildNames() {
        return getData().getChildData().keySet();
    }

    public ListenerRegistration addAvailableChildrenListener(AvailableChildrenListener<? super OBJECT> listener, boolean callForExisting) {
        ListenerRegistration result = availableChildrenListeners.addListener(listener);
        if(callForExisting)
            for(ChildOverview cd : childOverviews.values())
                listener.childAdded(getThis(), cd);
        return result;
    }

    protected void addChildLoadedListener(String childId, ChildLoadedListener<? super OBJECT, ? super CHILD> listener) {
        CHILD object = getChild(childId);
        if(object != null)
            listener.childLoaded(getThis(), object);
        else {
            Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>> listeners = childLoadedListeners.get(childId);
            if(listeners == null) {
                listeners = getListenersFactory().create();
                childLoadedListeners.put(childId, listeners);
            }
            addListenerRegistration(listeners.addListener(listener));
        }
    }

    public void loadChildOverviews() {
        sendMessage(CHILD_OVERVIEWS_REQUEST, NoPayload.INSTANCE);
    }

    /**
     * Makes a request to load the child object for the given id
     * @param manager the load manager used to specify what to load and notify about failures or when objects are loaded
     */
    public void load(LoadManager manager) {
        if(manager == null)
            throw new HousemateCommsException("Null manager");
        else if(manager.getToLoad().size() == 0)
            manager.succeeded();
        else {
            String id = Integer.toString(nextLoaderId++);
            pendingLoads.put(id, manager);
            sendMessage(RemoteObject.LOAD_REQUEST, new LoadRequest(id, manager.getToLoad()));
        }
    }

    protected boolean alreadyLoaded(TreeLoadInfo tree) {
        CHILD child = getChild(tree.getId());
        if(child == null)
            return false;
        else
            for(TreeLoadInfo childTree : tree.getChildren().values())
                if(!child.alreadyLoaded(childTree))
                    return false;
        return true;
    }

    /**
     * Gets this object as its sub class type
     * @return this object as its sub class type
     */
    protected final OBJECT getThis() {
        return (OBJECT)this;
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        // call all child loaded listeners about this object
        Listeners<ChildLoadedListener<? super OBJECT, ? super CHILD>> listeners = childLoadedListeners.get(child.getId());
        if(listeners != null) {
            for(ChildLoadedListener<? super OBJECT, ? super CHILD> listener : listeners)
                listener.childLoaded(getThis(), child);
        }
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // do nothing
    }

    /**
     *
     * Factory for housemate objects
     */
    public interface Factory<DATA extends HousemateData<?>, O extends ProxyObject<? extends DATA, ?, ?, ?, ?>> extends ObjectFactory<DATA, O> {}
}
