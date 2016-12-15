package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealListGeneratedImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Types;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class FeatureDriverType extends FactoryType<FeatureDriver.Factory<?>> {

    public final static String TYPE_ID = "feature-factory";
    public final static String TYPE_NAME = "Feature Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new feature";

    @Inject
    protected FeatureDriverType(@Types Logger logger, ListenersFactory listenersFactory,
                                RealOptionImpl.Factory optionFactory, RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, listenersFactory, optionFactory, optionsFactory);
    }
}
