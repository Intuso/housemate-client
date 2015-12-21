package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ValueData;
import com.intuso.housemate.object.v1_0.api.Feature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Base interface for all proxy features
 * @param <FEATURE> the feature type
 */
public abstract class ProxyFeature<
        COMMANDS extends ProxyList<CommandData, ? extends ProxyCommand<?, ?, ?, ?>, COMMANDS>,
        VALUES extends ProxyList<ValueData, ? extends ProxyValue<?, ?>, VALUES>,
        FEATURE extends ProxyFeature<COMMANDS, VALUES, FEATURE>>
        extends ProxyObject<FeatureData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, FEATURE, Feature.Listener<? super FEATURE>>
        implements Feature<COMMANDS, VALUES, FEATURE> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyFeature(Logger logger, ListenersFactory listenersFactory, FeatureData data) {
        super(listenersFactory, logger, data);
    }

    @Override
    public final COMMANDS getCommands() {
        return (COMMANDS) getChild(FeatureData.COMMANDS_ID);
    }

    @Override
    public final VALUES getValues() {
        return (VALUES) getChild(FeatureData.VALUES_ID);
    }
}
