package com.intuso.housemate.client.v1_0.real.impl.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.real.api.RealCondition;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealConditionImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealParameterImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.TypeRepository;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddConditionCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new condition";

    public interface Callback {
        void addCondition(RealConditionImpl condition);
    }

    public static class Factory {

        private final RealCommandImpl.Factory commandFactory;
        private final TypeRepository typeRepository;
        private final RealParameterImpl.Factory parameterFactory;
        private final Performer.Factory performerFactory;

        @Inject
        public Factory(RealCommandImpl.Factory commandFactory,
                       TypeRepository typeRepository,
                       RealParameterImpl.Factory parameterFactory, Performer.Factory performerFactory) {
            this.commandFactory = commandFactory;
            this.typeRepository = typeRepository;
            this.parameterFactory = parameterFactory;
            this.performerFactory = performerFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealCondition.RemoveCallback<RealConditionImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, performerFactory.create(baseLogger, callback, removeCallback),
                    Lists.newArrayList(
                            parameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    typeRepository.getType(new TypeSpec(String.class)),
                                    1,
                                    1),
                            parameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    typeRepository.getType(new TypeSpec(String.class)),
                                    1,
                                    1)));
        }
    }

    public static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final Callback callback;
        private final RealCondition.RemoveCallback<RealConditionImpl> removeCallback;
        private final RealConditionImpl.Factory conditionFactory;

        @Inject
        public Performer(@Assisted Logger logger,
                         @Assisted Callback callback,
                         @Assisted RealCondition.RemoveCallback<RealConditionImpl> removeCallback,
                         RealConditionImpl.Factory conditionFactory) {
            this.logger = logger;
            this.callback = callback;
            this.conditionFactory = conditionFactory;
            this.removeCallback = removeCallback;
        }

        @Override
        public void perform(Type.InstanceMap values) {
            Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
            if(name == null || name.getFirstValue() == null)
                throw new HousemateException("No name specified");
            Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
            if(description == null || description.getFirstValue() == null)
                throw new HousemateException("No description specified");
            RealConditionImpl condition = conditionFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addCondition(condition);
        }

        public interface Factory {
            Performer create(Logger logger,
                             Callback callback,
                             RealCondition.RemoveCallback<RealConditionImpl> removeCallback);
        }
    }
}
