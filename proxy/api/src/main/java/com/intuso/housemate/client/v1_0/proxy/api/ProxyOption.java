package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.object.v1_0.api.Option;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyOption(Log log, ListenersFactory listenersFactory, OptionData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return getChild(OptionData.SUB_TYPES_ID);
    }
}
