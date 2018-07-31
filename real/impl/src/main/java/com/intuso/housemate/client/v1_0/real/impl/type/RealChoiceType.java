package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealListGeneratedImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealOptionImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealTypeImpl<O>
        implements Option.Container<RealListGeneratedImpl<? extends RealOptionImpl>> {

    public final static String OPTIONS = "options";

    protected final RealListGeneratedImpl<RealOptionImpl> options;

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param managedCollectionFactory
     * @param options the type's options
     */
    protected RealChoiceType(@Assisted Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted Iterable<RealOptionImpl> options,
                             ManagedCollectionFactory managedCollectionFactory,
                             RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(logger, new ChoiceData(id, name, description), managedCollectionFactory);
        this.options = optionsFactory.create(logger,
                OPTIONS,
                OPTIONS,
                "The options for the choice",
                options
        );
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        options.init(ChildUtil.name(name, OPTIONS), senderFactory, receiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        options.uninit();
    }

    @Override
    public RealListGeneratedImpl<RealOptionImpl> getOptions() {
        return options;
    }

    public interface Factory {
        <O> RealChoiceType<O> create(Logger logger,
                                     @Assisted("id") String id,
                                     @Assisted("name") String name,
                                     @Assisted("description") String description,
                                     Iterable<RealOptionImpl> options);
    }
}
