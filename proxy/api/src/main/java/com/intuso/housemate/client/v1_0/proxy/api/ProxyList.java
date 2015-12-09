package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.object.v1_0.api.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import com.intuso.utilities.object.ObjectListener;

import java.util.Iterator;

/**
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <LIST> the type of the list
 */
public abstract class ProxyList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            LIST extends ProxyList<CHILD_DATA, CHILD, LIST>>
        extends ProxyObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, LIST, List.Listener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyList(Logger logger, ListenersFactory listenersFactory, ListData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super CHILD> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(CHILD element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Message.Receiver<CHILD_DATA>() {
            @Override
            public void messageReceived(Message<CHILD_DATA> message) {
                CHILD child = createChildInstance(message.getPayload());
                if(child != null) {
                    child.init(ProxyList.this);
                    addChild(child);
                } else
                    throw new HousemateCommsException("Could not create new list element");
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Message.Receiver<HousemateData>() {
            @Override
            public void messageReceived(Message<HousemateData> message) {
                CHILD child = getChild(message.getPayload().getId());
                if(child != null) {
                    child.uninit();
                    removeChild(child.getId());
                }
            }
        }));
        return result;
    }

    @Override
    public final CHILD get(String id) {
        return getChild(id);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        super.childObjectAdded(childId, child);
        for(List.Listener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        super.childObjectRemoved(childId, child);
        for(List.Listener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }
}
