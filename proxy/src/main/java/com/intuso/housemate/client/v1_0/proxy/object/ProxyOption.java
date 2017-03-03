package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Option;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <SUB_TYPES> the type of the sub types
 * @param <OPTION> the type of the option
 */
public abstract class ProxyOption<
            SUB_TYPES extends ProxyList<? extends ProxySubType<?, ?>, ?>,
            OPTION extends ProxyOption<SUB_TYPES, OPTION>>
        extends ProxyObject<Option.Data, Option.Listener<? super OPTION>>
        implements Option<SUB_TYPES, OPTION> {

    private final SUB_TYPES subTypes;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyOption(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       ProxyObject.Factory<SUB_TYPES> subTypesFactory) {
        super(logger, Option.Data.class, managedCollectionFactory, receiverFactory);
        subTypes = subTypesFactory.create(ChildUtil.logger(logger, SUB_TYPES_ID));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        subTypes.init(ChildUtil.name(name, SUB_TYPES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public SUB_TYPES getSubTypes() {
        return subTypes;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(SUB_TYPES_ID.equals(id))
            return subTypes;
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:17
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyOption<ProxyList.Simple<ProxySubType.Simple>, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyList.Simple<ProxySubType.Simple>> subTypesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, subTypesFactory);
        }
    }
}
