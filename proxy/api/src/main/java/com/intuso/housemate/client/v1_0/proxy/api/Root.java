package com.intuso.housemate.client.v1_0.proxy.api;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;

/**
 * Created by tomc on 25/11/16.
 */
public interface Root {
    void start();
    void stop();

    class Service extends AbstractIdleService {

        private final Root root;

        @Inject
        public Service(Root root) {
            this.root = root;
        }

        @Override
        protected void startUp() throws Exception {
            root.start();
        }

        @Override
        protected void shutDown() throws Exception {
            root.stop();
        }
    }
}
