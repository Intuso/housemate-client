package com.intuso.housemate.client.v1_0.proxy.simple.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyClientHelper;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyRoot;
import com.intuso.housemate.comms.v1_0.api.Router;

/**
 * Created by tomc on 22/12/15.
 */
public class HelperProvider implements Provider<ProxyClientHelper<SimpleProxyRoot>> {

    private final SimpleProxyRoot root;
    private final Router<?> router;

    @Inject
    public HelperProvider(SimpleProxyRoot root, Router<?> router) {
        this.root = root;
        this.router = router;
    }

    @Override
    public ProxyClientHelper<SimpleProxyRoot> get() {
        return new ProxyClientHelper<>(root, router);
    }
}
