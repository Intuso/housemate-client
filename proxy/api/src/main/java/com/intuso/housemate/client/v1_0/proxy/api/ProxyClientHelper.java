package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.collect.Lists;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/01/14
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class ProxyClientHelper<ROOT extends ProxyRoot<?, ?, ?>> {

    private final static Logger logger = LoggerFactory.getLogger(ProxyClientHelper.class);

    private final ROOT proxyRoot;
    private final Router<?> router;

    private ApplicationDetails applicationDetails;
    private List<TreeLoadInfo> toLoad = Lists.newArrayList();
    private LoadManager.Callback callback;

    private boolean shouldBeConnected = false;
    private boolean shouldClearRoot = true;
    private boolean shouldLoad = true;
    private ListenerRegistration proxyListenerRegistration;
    private ListenerRegistration routerListenerRegistration;

    public ProxyClientHelper(ROOT proxyRoot, Router<?> router) {
        this.proxyRoot = proxyRoot;
        this.router = router;
    }

    public ROOT getRoot() {
        return proxyRoot;
    }

    public ProxyClientHelper<ROOT> applicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
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
        shouldBeConnected = true;
        proxyListenerRegistration = proxyRoot.addObjectListener(new ProxyRootListener());
        RouterListener routerListener = new RouterListener();
        routerListenerRegistration = router.addListener(routerListener);
        router.connect();
        proxyRoot.register(applicationDetails);
        return this;
    }

    public void unregister() {
        if(proxyRoot.getApplicationInstanceStatus() != ApplicationInstance.Status.Unregistered)
            proxyRoot.unregister();
    }

    public void stop() {
        shouldBeConnected = true;
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
        public void serverConnectionStatusChanged(Router root, ConnectionStatus connectionStatus) {
            logger.info("Server connection status: {}", connectionStatus);
            if(connectionStatus == ConnectionStatus.DisconnectedPermanently && shouldBeConnected)
                router.connect();
            else if(connectionStatus == ConnectionStatus.ConnectedToServer && proxyRoot.getApplicationInstanceStatus() == ApplicationInstance.Status.Unregistered)
                proxyRoot.register(applicationDetails);
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
            logger.info("Application status: {}", applicationStatus);
        }

        @Override
        public void applicationInstanceStatusChanged(ProxyRoot<?, ?, ?> root, ApplicationInstance.Status applicationInstanceStatus) {
            logger.info("Application instance status: {}", applicationInstanceStatus);
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
