package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.RealHardware;
import com.intuso.housemate.client.v1_0.real.impl.BasicRealRoot;
import com.intuso.housemate.client.v1_0.real.impl.RealObject;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.v1_0.real.impl.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationData;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.RootData;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestRealRoot extends BasicRealRoot {

    @Inject
    public TestRealRoot(ListenersFactory listenersFactory,
                        PropertyRepository properties,
                        Router<?> router,
                        AddHardwareCommand.Factory addHardwareCommandFactory,
                        AddDeviceCommand.Factory addDeviceCommandFactory,
                        DeviceFactoryType deviceFactoryType,
                        HardwareFactoryType hardwareFactoryType,
                        RealDevice.Factory deviceFactory,
                        RealHardware.Factory hardwareFactory) {
        super(listenersFactory, properties, router, addHardwareCommandFactory, addDeviceCommandFactory,
                deviceFactoryType, hardwareFactoryType, deviceFactory, hardwareFactory);
        try {
            distributeMessage(new Message<Message.Payload>(new String[] {""}, RootData.APPLICATION_STATUS_TYPE, new ApplicationData.StatusPayload(Application.Status.AllowInstances)));
            distributeMessage(new Message<Message.Payload>(new String[] {""}, RootData.APPLICATION_INSTANCE_STATUS_TYPE, new ApplicationInstanceData.StatusPayload(ApplicationInstance.Status.Allowed)));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void connect() {
        // do nothing
    }

    public void init() {
        addType(new StringType(getListenersFactory()));
        addType(new IntegerType(getListenersFactory()));
        addType(new BooleanType(getListenersFactory()));
    }

    public void addWrapper(RealObject<?, ?, ?, ?> wrapper) {
        removeChild(wrapper.getId());
        super.addChild(wrapper);
        wrapper.init(this);
    }
}
