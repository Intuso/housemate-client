package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.RegexTypeData;
import com.intuso.housemate.object.v1_0.api.RegexMatcher;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyRegexType<
            TYPE extends ProxyRegexType<TYPE>>
        extends ProxyType<RegexTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    private final RegexMatcher regexMatcher;

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     * @param regexMatcher
     */
    public ProxyRegexType(Log log, ListenersFactory listenersFactory, RegexTypeData data, RegexMatcher regexMatcher) {
        super(log, listenersFactory, data);
        this.regexMatcher = regexMatcher;
    }

    /**
     * Checks that the given value matches the regex for this type
     * @param value the value to check
     * @return true if the value matches the regex
     */
    public final boolean isCorrectFormat(String value) {
        return regexMatcher.matches(value);
    }
}
