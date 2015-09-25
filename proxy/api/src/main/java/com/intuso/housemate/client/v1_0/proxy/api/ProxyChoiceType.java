package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.object.v1_0.api.Option;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <OPTION> the type of the options
 * @param <OPTIONS> the type of the options list
 * @param <TYPE> the type of the type
 */
public abstract class ProxyChoiceType<
            OPTION extends ProxyOption<?, ?, OPTION>,
            OPTIONS extends ProxyList<OptionData, OPTION, OPTIONS>,
            TYPE extends ProxyChoiceType<OPTION, OPTIONS, TYPE>>
        extends ProxyType<ChoiceTypeData, ListData<OptionData>, OPTIONS, TYPE>
        implements Option.Container<OPTIONS> {

    private static final String OPTIONS_ID = "options";

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyChoiceType(Log log, ListenersFactory listenersFactory, ChoiceTypeData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public OPTIONS getOptions() {
        return getChild(OPTIONS_ID);
    }
}
