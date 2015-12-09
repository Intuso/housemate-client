package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealOption;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.comms.v1_0.api.payload.ChoiceTypeData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.object.v1_0.api.Option;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealTypeImpl<ChoiceTypeData, ListData<OptionData>, O>
        implements Option.Container<RealList<? extends RealOption>> {

    public final static String OPTIONS = "options";

    protected final RealListImpl<OptionData, RealOptionImpl> options;

    /**
     * @param logger the log
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, RealOptionImpl... options) {
        this(logger, listenersFactory, id, name, description, minValues, maxValues, Arrays.asList(options));
    }

    /**
     * @param logger the log
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, List<RealOptionImpl> options) {
        super(logger, listenersFactory, new ChoiceTypeData(id, name, description, minValues, maxValues));
        this.options = new RealListImpl<>(logger, listenersFactory, OPTIONS, OPTIONS, "The options for the choice", options);
        addChild(this.options);
    }

    @Override
    public RealList<? extends RealOption> getOptions() {
        return options;
    }
}
