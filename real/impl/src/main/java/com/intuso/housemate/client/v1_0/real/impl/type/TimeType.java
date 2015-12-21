package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.type.Time;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for a time
 */
public class TimeType extends RealRegexType<Time> {

    public final static String ID = "time";
    public final static String NAME = "Time";
    public final static String DESCRIPTION = "A time in 24 hour format, eg 01:23:45";
    public final static String REGEX = "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    private final static Logger logger = LoggerFactory.getLogger(TimeType.class);

    @Inject
    public TimeType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1, REGEX);
    }

    @Override
    public TypeInstance serialise(Time s) {
        return s != null ? new TypeInstance(s.toString()) : null;
    }

    @Override
    public Time deserialise(TypeInstance value) throws HousemateCommsException {
        if(value == null || value.getValue() == null)
            return null;
        String parts[] = value.getValue().split(":");
        int hours, minutes = 0, seconds = 0;
        try {
            hours = Integer.parseInt(parts[0]);
            if(parts.length > 1)
                minutes = Integer.parseInt(parts[1]);
            if(parts.length > 2)
                seconds = Integer.parseInt(parts[2]);
        } catch(NumberFormatException e) {
            throw new HousemateCommsException("Failed to parse time from " + value, e);
        }
        return new Time(hours, minutes, seconds);
    }
}
