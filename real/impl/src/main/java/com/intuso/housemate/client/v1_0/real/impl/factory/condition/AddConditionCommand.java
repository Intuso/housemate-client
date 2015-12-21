package com.intuso.housemate.client.v1_0.real.impl.factory.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealCondition;
import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.housemate.client.v1_0.real.impl.LoggerUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddConditionCommand extends RealCommandImpl {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new condition";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new condition";

    private final Callback callback;
    private final ConditionFactoryType conditionFactoryType;
    private final RealCondition.Factory conditionFactory;
    private final RealCondition.RemoveCallback removeCallback;

    @Inject
    protected AddConditionCommand(ListenersFactory listenersFactory,
                                  StringType stringType,
                                  ConditionFactoryType conditionFactoryType,
                                  RealCondition.Factory conditionFactory,
                                  @Assisted Logger logger,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  @Assisted Callback callback,
                                  @Assisted RealCondition.RemoveCallback removeCallback) {
        super(logger, listenersFactory, id, name, description,
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, NAME_PARAMETER_ID), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, DESCRIPTION_PARAMETER_ID), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, TYPE_PARAMETER_ID), TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, conditionFactoryType));
        this.callback = callback;
        this.conditionFactoryType = conditionFactoryType;
        this.conditionFactory = conditionFactory;
        this.removeCallback = removeCallback;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealCondition<?> condition = conditionFactory.create(LoggerUtil.child(getLogger(), name.getFirstValue()), new ConditionData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback);
        callback.addCondition(condition);
        TypeInstances conditionType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(conditionType != null && conditionType.getFirstValue() != null)
            ((RealProperty)condition.getDriverProperty()).setTypedValues(conditionFactoryType.deserialise(conditionType.getElements().get(0)));
    }

    public interface Callback {
        void addCondition(RealCondition condition);
    }

    public interface Factory {
        AddConditionCommand create(Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   Callback callback,
                                   RealCondition.RemoveCallback removeCallback);
    }
}
