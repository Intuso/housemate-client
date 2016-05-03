package com.intuso.housemate.client.v1_0.real.impl.type;

import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealListImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Arrays;
import java.util.List;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealTypeImpl<O>
        implements Option.Container<RealListImpl<? extends RealOptionImpl>> {

    public final static String OPTIONS = "options";

    protected final RealListImpl<RealOptionImpl> options;

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, int minValues,
                             int maxValues, RealOptionImpl... options) {
        this(logger, id, name, description, listenersFactory, minValues, maxValues, Arrays.asList(options));
    }

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, int minValues,
                             int maxValues, List<RealOptionImpl> options) {
        super(logger, new ChoiceData(id, name, description, minValues, maxValues), listenersFactory);
        this.options = new RealListImpl<>(logger, new com.intuso.housemate.client.v1_0.api.object.List.Data(OPTIONS, OPTIONS, "The options for the choice"), listenersFactory, options);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        options.init(ChildUtil.name(name, OPTIONS), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        options.uninit();
    }

    @Override
    public RealListImpl<RealOptionImpl> getOptions() {
        return options;
    }
}
