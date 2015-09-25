package com.intuso.housemate.client.v1_0.real.api.impl.type;

import com.google.inject.Inject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Type for a set of days
 */
public class DaysType extends EnumChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public DaysType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, ID, NAME, "Selection of days", 0, -1, Day.class, Day.values());
    }
}
