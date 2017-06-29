package com.intuso.housemate.client.v1_0.proxy.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.view.DeviceConnectedView;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base interface for all proxy features
 * @param <DEVICE> the feature type
 */
public abstract class ProxyDeviceConnected<COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        DEVICE extends ProxyDeviceConnected<COMMAND, COMMANDS, VALUES, DEVICE>>
        extends ProxyDevice<Device.Connected.Data, Device.Connected.Listener<? super DEVICE>, DeviceConnectedView, COMMAND, COMMANDS, VALUES, DEVICE>
        implements Device.Connected<COMMAND, COMMANDS, VALUES, DEVICE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceConnected(Logger logger,
                                String name,
                                ManagedCollectionFactory managedCollectionFactory,
                                Receiver.Factory receiverFactory,
                                Factory<COMMAND> commandFactory,
                                Factory<COMMANDS> commandsFactory,
                                Factory<VALUES> valuesFactory) {
        super(logger, name, Device.Connected.Data.class, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valuesFactory);
    }

    @Override
    public DeviceConnectedView createView() {
        return new DeviceConnectedView();
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyDeviceConnected<ProxyCommand.Simple,
            ProxyList.Simple<ProxyCommand.Simple>,
            ProxyList.Simple<ProxyValue.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valuesFactory);
        }
    }
}
