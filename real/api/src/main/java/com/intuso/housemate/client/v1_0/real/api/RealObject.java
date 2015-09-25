package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.object.v1_0.api.ObjectListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

public abstract class RealObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends RemoteObject<DATA, CHILD_DATA, CHILD, LISTENER>
        implements com.intuso.utilities.object.ObjectListener<CHILD> {

    private RealRoot realRoot;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    protected RealObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(log, listenersFactory, data);
    }

    /**
     * Sends a message to the server
     * @param type the type of the message
     * @param payload the message payload
     * @param <MV> the type of the message's payload
     */
    protected final <MV extends Message.Payload> void sendMessage(String type, MV payload) {
        getRealRoot().sendMessage(new Message<>(
                getPath(), type, payload));
    }

    /**
     * Gets the root object for this object
     * @return the root object for this object
     */
    protected RealRoot getRealRoot() {
        return realRoot;
    }

    @Override
    protected void initPreRecurseHook(RemoteObject<?, ?, ?, ?> parent) {

        // get the server for this object
        // unnecessary RealObject cast is to prevent IntelliJ showing this as an error.
        if((RealObject)this instanceof RealRoot)
            realRoot = (RealRoot)(RealObject)this;
        else if(parent != null && parent instanceof RealObject)
            realRoot = ((RealObject)parent).realRoot;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        return result;
    }

    @Override
    public void childObjectAdded(String childName, CHILD child) {
        child.init(this);
        sendMessage(ADD_TYPE, child.getData());
    }

    @Override
    public void childObjectRemoved(String name, CHILD child) {
        child.uninit();
        sendMessage(REMOVE_TYPE, child.getData());
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // don't need to worry about ancestors other than children, handled above
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        // don't need to worry about ancestors other than children, handled above
    }
}
