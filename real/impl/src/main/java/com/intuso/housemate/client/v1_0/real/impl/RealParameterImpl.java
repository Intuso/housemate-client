package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <O> the type of the parameter's value
 */
public final class RealParameterImpl<O>
        extends RealObject<Parameter.Data, Parameter.Listener<? super RealParameterImpl<O>>>
        implements RealParameter<O, RealTypeImpl<O>, RealParameterImpl<O>> {

    private RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param type the type of the parameter's value
     */
    @Inject
    public RealParameterImpl(@Assisted final Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted RealTypeImpl type,
                             @Assisted("min") int minValues,
                             @Assisted("max") int maxValues,
                             ListenersFactory listenersFactory) {
        super(logger, new Parameter.Data(id, name, description, type.getId(), minValues, maxValues), listenersFactory);
        this.type = type;
    }

    @Override
    public final RealTypeImpl<O> getType() {
        return type;
    }

    public interface Factory {
        RealParameterImpl<?> create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    RealTypeImpl type,
                                    @Assisted("min") int minValues,
                                    @Assisted("max") int maxValues);
    }
}
