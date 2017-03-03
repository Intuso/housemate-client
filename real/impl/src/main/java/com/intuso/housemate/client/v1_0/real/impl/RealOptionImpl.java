package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealOption;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public final class RealOptionImpl
        extends RealObject<Option.Data, Option.Listener<? super RealOptionImpl>>
        implements RealOption<RealListGeneratedImpl<RealSubTypeImpl<?>>, RealOptionImpl> {

    private final RealListGeneratedImpl<RealSubTypeImpl<?>> subTypes;

    /**
     * @param logger {@inheritDoc}
     * @param subTypes the option's sub types
     * @param managedCollectionFactory
     */
    @Inject
    public RealOptionImpl(@Assisted Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted Iterable<RealSubTypeImpl<?>> subTypes,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          RealListGeneratedImpl.Factory<RealSubTypeImpl<?>> subTypesFactory) {
        super(logger, new Option.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID),
                Option.SUB_TYPES_ID,
                "Sub Types",
                "The sub types of this option",
                subTypes);
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        subTypes.init(ChildUtil.name(name, Option.SUB_TYPES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public final RealListGeneratedImpl<RealSubTypeImpl<?>> getSubTypes() {
        return subTypes;
    }

    public interface Factory {
        RealOptionImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              Iterable<RealSubTypeImpl<?>> subTypes);
    }
}
