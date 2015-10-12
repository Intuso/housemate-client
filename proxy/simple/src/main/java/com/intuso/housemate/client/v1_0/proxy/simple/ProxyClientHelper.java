package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/01/14
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class ProxyClientHelper<ROOT extends ProxyRoot<?, ?, ?>> {

    private final Log log;
    private final ROOT proxyRoot;
    private final Router<?> router;

    private ApplicationDetails applicationDetails;
    private String component;
    private List<TreeLoadInfo> toLoad = Lists.newArrayList();
    private LoadManager.Callback callback;

    private boolean shouldClearRoot = true;
    private boolean shouldLoad = true;
    private ListenerRegistration proxyListenerRegistration;
    private ListenerRegistration routerListenerRegistration;

    private ProxyClientHelper(Log log, ROOT proxyRoot, Router<?> router) {
        this.log = log;
        this.proxyRoot = proxyRoot;
        this.router = router;
    }

    public static <ROOT extends ProxyRoot<?, ?, ?>> ProxyClientHelper<ROOT>
                newClientHelper(Log log, ROOT proxyRoot, Router<?> router) {
        return new ProxyClientHelper<>(log, proxyRoot, router);
    }

    public static ProxyClientHelper<SimpleProxyRoot>
                newClientHelper(Injector injector) {
        return new ProxyClientHelper<>(
                injector.getInstance(Log.class),
                injector.getInstance(SimpleProxyRoot.class),
                injector.getInstance(new Key<Router<?>>() {}));
    }

    public static ProxyClientHelper<SimpleProxyRoot>
                newClientHelper(Module... modules) {
        return ProxyClientHelper.newClientHelper(Guice.createInjector(modules));
    }

    public ROOT getRoot() {
        return proxyRoot;
    }

    public ProxyClientHelper<ROOT> applicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
        return this;
    }

    public ProxyClientHelper<ROOT> component(String component) {
        this.component = component;
        return this;
    }

    public ProxyClientHelper<ROOT> load(TreeLoadInfo treeLoadInfo) {
        toLoad.add(treeLoadInfo);
        return this;
    }

    private ProxyClientHelper<ROOT> load(String[] path, String ending) {
        return load(TreeLoadInfo.create(path, ending));
    }

    public ProxyClientHelper<ROOT> load(String ... path) {
        return load(path, null);
    }

    public ProxyClientHelper<ROOT> loadAllChildren(String ... path) {
        return load(path, RemoteObject.EVERYTHING);
    }

    public ProxyClientHelper<ROOT> loadAllDescendants(String... path) {
        return load(path, RemoteObject.EVERYTHING_RECURSIVE);
    }

    public ProxyClientHelper<ROOT> callback(LoadManager.Callback callback) {
        this.callback = callback;
        return this;
    }

    public ProxyClientHelper<ROOT> start() {
        proxyListenerRegistration = proxyRoot.addObjectListener(new ProxyRootListener());
        RouterListener routerListener = new RouterListener();
        routerListenerRegistration = router.addListener(routerListener);
        routerListener.serverConnectionStatusChanged(null, ServerConnectionStatus.DisconnectedPermanently);
        return this;
    }

    public void unregister() {
        if(proxyRoot.getApplicationInstanceStatus() != ApplicationInstance.Status.Unregistered)
            proxyRoot.unregister();
    }

    public void stop() {
        router.disconnect();
        if(proxyListenerRegistration != null)
            proxyListenerRegistration.removeListener();
        if(routerListenerRegistration != null)
            routerListenerRegistration.removeListener();
    }

    public Router<?> getRouter() {
        return router;
    }

    private class RouterListener implements Router.Listener<Router> {

        @Override
        public void serverConnectionStatusChanged(Router root, ServerConnectionStatus serverConnectionStatus) {
            log.d("Router serverConnectionStatus = " + serverConnectionStatus);
            if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently) {
                router.connect();
                proxyRoot.register(applicationDetails, component);
            }
        }

        @Override
        public void newServerInstance(Router root, String serverId) {
            shouldClearRoot = true;
            shouldLoad = true;
        }
    }

    private class ProxyRootListener implements ProxyRoot.Listener<ProxyRoot<?, ?, ?>> {

        @Override
        public void applicationStatusChanged(ProxyRoot<?, ?, ?> root, Application.Status applicationStatus) {
            log.d("Root applicationStatus = " + applicationStatus);
        }

        @Override
        public void applicationInstanceStatusChanged(ProxyRoot<?, ?, ?> root, ApplicationInstance.Status applicationInstanceStatus) {
            log.d("Root applicationInstanceStatus = " + applicationInstanceStatus);
            if(applicationInstanceStatus == ApplicationInstance.Status.Allowed) {
                if(shouldClearRoot) {
                    proxyRoot.clearLoadedObjects();
                    shouldClearRoot = false;
                }
                if(shouldLoad) {
                    proxyRoot.load(new LoadManager(callback, toLoad));
                    shouldLoad = false;
                }
            }
        }

        @Override
        public void newApplicationInstance(ProxyRoot<?, ?, ?> root, String instanceId) {
            // do nothing, saved in router listener
        }
    }
}
