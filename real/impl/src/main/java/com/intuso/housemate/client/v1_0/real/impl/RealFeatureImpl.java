package com.intuso.housemate.client.v1_0.real.impl;

import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealFeature;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.object.v1_0.api.Feature;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for all devices
 */
public final class RealFeatureImpl
        extends RealObject<
        FeatureData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Feature.Listener<? super RealFeature>>
        implements RealFeature {

    private final static String COMMANDS_DESCRIPTION = "The feature's commands";
    private final static String VALUES_DESCRIPTION = "The feature's values";

    private final RealList<RealCommand> commands;
    private final RealList<RealValue<?>> values;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     */
    public RealFeatureImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, new FeatureData(id, name, description));
        this.commands = (RealList)new RealListImpl<>(log, listenersFactory, FeatureData.COMMANDS_ID, FeatureData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = (RealList)new RealListImpl<>(log, listenersFactory, FeatureData.VALUES_ID, FeatureData.VALUES_ID, VALUES_DESCRIPTION);
        addChild((RealListImpl)commands);
        addChild((RealListImpl)values);
    }

    @Override
    public final RealList<RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<RealValue<?>> getValues() {
        return values;
    }
}
