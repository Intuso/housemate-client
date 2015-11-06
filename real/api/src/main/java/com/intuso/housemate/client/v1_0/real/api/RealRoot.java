package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.RequiresAccess;
import com.intuso.housemate.object.v1_0.api.ObjectLifecycleListener;
import com.intuso.housemate.object.v1_0.api.ObjectRoot;
import com.intuso.housemate.object.v1_0.api.Root;
import com.intuso.utilities.listener.ListenerRegistration;

public interface RealRoot
        extends Root<RealRoot.Listener, RealRoot>,
        RequiresAccess,
        Message.Sender,
        ObjectRoot<RealRoot.Listener, RealRoot>,
        RealApplication.Container,
        RealAutomation.Container,
        RealDevice.Container,
        RealHardware.Container,
        RealType.Container,
        RealUser.Container {

    RealCommand getAddHardwareCommand();

    RealCommand getAddDeviceCommand();

    RealCommand getAddAutomationCommand();

    RealCommand getAddUserCommand();

    ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener);

    interface Listener extends Root.Listener<RealRoot>, RequiresAccess.Listener<RealRoot> {}
}
