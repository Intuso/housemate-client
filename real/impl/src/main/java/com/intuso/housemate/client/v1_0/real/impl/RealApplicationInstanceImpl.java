package com.intuso.housemate.client.v1_0.real.impl;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.real.api.RealApplicationInstance;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.client.v1_0.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

public class RealApplicationInstanceImpl
        extends RealObject<
        ApplicationInstanceData,
        HousemateData<?>,
            RealObject<?, ? ,?, ?>,
            ApplicationInstance.Listener<? super RealApplicationInstance>>
        implements RealApplicationInstance {

    private final RealCommandImpl allowCommand;
    private final RealCommandImpl rejectCommand;
    private final RealValueImpl<Status> statusValue;

    public RealApplicationInstanceImpl(Logger logger, ListenersFactory listenersFactory, String instanceId,
                                       ApplicationInstanceStatusType applicationInstanceStatusType) {
        super(logger, listenersFactory, new ApplicationInstanceData(instanceId, instanceId, instanceId));
        allowCommand = new RealCommandImpl(logger, listenersFactory, ApplicationInstanceData.ALLOW_COMMAND_ID, ApplicationInstanceData.ALLOW_COMMAND_ID, "Allow access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(Status.Allowed);
            }
        };
        rejectCommand = new RealCommandImpl(logger, listenersFactory, ApplicationInstanceData.REJECT_COMMAND_ID, ApplicationInstanceData.REJECT_COMMAND_ID, "Reject access to the application instance",
                Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                statusValue.setTypedValues(Status.Rejected);
            }
        };
        statusValue = new RealValueImpl<>(logger, listenersFactory, ApplicationInstanceData.STATUS_VALUE_ID, ApplicationInstanceData.STATUS_VALUE_ID,
                "The status of the application instance", applicationInstanceStatusType, (List)null);
        addChild(allowCommand);
        addChild(rejectCommand);
        addChild(statusValue);
    }

    @Override
    public RealCommand getAllowCommand() {
        return allowCommand;
    }

    @Override
    public RealCommand getRejectCommand() {
        return rejectCommand;
    }

    @Override
    public RealValue<Status> getStatusValue() {
        return statusValue;
    }
}
