package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public class RealRegexType extends RealTypeImpl<String> {

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param managedCollectionFactory
     * @param regexPattern the regex pattern that values must match
     */
    @Inject
    protected RealRegexType(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted("regexPattern") String regexPattern,
                            ManagedCollectionFactory managedCollectionFactory,
                            Sender.Factory senderFactory) {
        super(logger,
                new RegexData(id, name, description, regexPattern),
                managedCollectionFactory,
                senderFactory);
    }

    @Override
    public Instance serialise(String value) {
        return value == null ? null : new Instance(value);
    }

    @Override
    public String deserialise(Instance instance) {
        return instance == null || instance.getValue() == null ? null : instance.getValue();
    }

    public interface Factory {
        RealRegexType create(Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted("regexPattern") String regexPattern);
    }
}
