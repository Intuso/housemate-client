package com.intuso.housemate.client.v1_0.real.impl.factory.automation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.client.v1_0.real.impl.LoggerUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.AutomationData;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddAutomationCommand extends RealCommandImpl {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new automation";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new automation";
    
    private final Callback callback;
    private final RealAutomation.Factory automationFactory;
    private final RealAutomation.RemoveCallback removeCallback;

    @Inject
    protected AddAutomationCommand(ListenersFactory listenersFactory,
                                   StringType stringType,
                                   RealAutomation.Factory automationFactory,
                                   @Assisted Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   @Assisted Callback callback,
                                   @Assisted RealAutomation.RemoveCallback removeCallback) {
        super(logger, listenersFactory, id, name, description,
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, NAME_PARAMETER_ID), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, DESCRIPTION_PARAMETER_ID), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType));
        this.callback = callback;
        this.automationFactory = automationFactory;
        this.removeCallback = removeCallback;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        callback.addAutomation(automationFactory.create(LoggerUtil.child(getLogger(), name.getFirstValue()), new AutomationData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback));
    }

    public interface Callback {
        void addAutomation(RealAutomation automation);
    }

    public interface Factory {
        AddAutomationCommand create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    Callback callback,
                                    RealAutomation.RemoveCallback removeCallback);
    }
}
