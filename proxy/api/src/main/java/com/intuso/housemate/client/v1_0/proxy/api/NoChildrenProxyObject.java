package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.object.v1_0.api.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public final class NoChildrenProxyObject extends ProxyObject<NoChildrenData, NoChildrenData, NoChildrenProxyObject,
        NoChildrenProxyObject, ObjectListener> {

    private NoChildrenProxyObject() {
        super(null, null, null);
    }

    @Override
    protected NoChildrenProxyObject createChild(NoChildrenData noChildrenData) {
        return null;
    }
}
