package com.intuso.housemate.client.v1_0.real.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.real.api.RealDeviceConnected;
import com.intuso.housemate.client.v1_0.real.impl.annotation.AnnotationParser;
import com.intuso.housemate.client.v1_0.real.impl.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public final class RealDeviceConnectedImpl
        extends RealDeviceImpl<Device.Connected.Data, Device.Connected.Listener<? super RealDeviceConnectedImpl>, RealDeviceConnectedImpl>
        implements RealDeviceConnected<RealCommandImpl,
        RealListGeneratedImpl<RealCommandImpl>,
        RealListGeneratedImpl<RealValueImpl<?>>,
        RealDeviceConnectedImpl> {

    @Inject
    public RealDeviceConnectedImpl(@Assisted final Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   ManagedCollectionFactory managedCollectionFactory,
                                   Sender.Factory senderFactory,
                                   AnnotationParser annotationParser,
                                   RealCommandImpl.Factory commandFactory,
                                   RealParameterImpl.Factory parameterFactory,
                                   RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                                   RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                                   TypeRepository typeRepository) {
        super(logger, new Device.Connected.Data(id, name, description), managedCollectionFactory, senderFactory,
                annotationParser, commandFactory, parameterFactory, commandsFactory, valuesFactory, typeRepository);
    }

    public interface Factory {
        RealDeviceConnectedImpl create(Logger logger,
                                       @Assisted("id") String id,
                                       @Assisted("name") String name,
                                       @Assisted("description") String description);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Device.Connected.Data, RealDeviceConnectedImpl> {

        private final RealDeviceConnectedImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealDeviceConnectedImpl create(Logger logger, Device.Connected.Data data, RealListPersistedImpl.RemoveCallback<RealDeviceConnectedImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription());
        }
    }
}
