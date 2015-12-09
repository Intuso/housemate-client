package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.object.v1_0.api.Option;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <SUB_TYPE> the type of the sub type
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            SUB_TYPE extends ProxySubType<?>,
            SUB_TYPES extends ProxyList<SubTypeData, SUB_TYPE, SUB_TYPES>,
            OPTION extends ProxyOption<SUB_TYPE, SUB_TYPES, OPTION>>
        extends ProxyObject<OptionData, ListData<SubTypeData>, SUB_TYPES, OPTION, Option.Listener<? super OPTION>>
        implements Option<SUB_TYPES, OPTION> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyOption(Logger logger, ListenersFactory listenersFactory, OptionData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return getChild(OptionData.SUB_TYPES_ID);
    }
}
