package com.intuso.housemate.client.v1_0.real.impl.factory.automation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Automation;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealAutomation;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealAutomationImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
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
    private final RealAutomation.Factory<RealAutomationImpl> automationFactory;
    private final RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback;

    @Inject
    protected AddAutomationCommand(ListenersFactory listenersFactory,
                                   StringType stringType,
                                   RealAutomation.Factory<RealAutomationImpl> automationFactory,
                                   @Assisted Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   @Assisted Callback callback,
                                   @Assisted RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback) {
        super(logger, id, name, description, listenersFactory,
                new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, listenersFactory, stringType),
                new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, listenersFactory, stringType));
        this.callback = callback;
        this.automationFactory = automationFactory;
        this.removeCallback = removeCallback;
    }

    @Override
    public void perform(Type.InstanceMap values) {
        Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        callback.addAutomation(automationFactory.create(ChildUtil.logger(logger, name.getFirstValue()), new Automation.Data(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback));
    }

    public interface Callback {
        void addAutomation(RealAutomationImpl automation);
    }

    public interface Factory {
        AddAutomationCommand create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    Callback callback,
                                    RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback);
    }
}
