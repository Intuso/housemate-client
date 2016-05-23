package com.intuso.housemate.client.v1_0.real.impl.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealUser;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealUserImpl;
import com.intuso.housemate.client.v1_0.real.impl.type.RegisteredTypes;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class AddUserCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new user";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new user";

    public interface Callback {
        void addUser(RealUserImpl user);
    }

    public static class Factory {

        private final RegisteredTypes registeredTypes;
        private final RealCommandImpl.Factory commandFactory;
        private final Performer.Factory performerFactory;

        @Inject
        public Factory(RegisteredTypes registeredTypes, RealCommandImpl.Factory commandFactory,
                       Performer.Factory performerFactory) {
            this.registeredTypes = registeredTypes;
            this.commandFactory = commandFactory;
            this.performerFactory = performerFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealUser.RemoveCallback<RealUserImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, performerFactory.create(baseLogger, callback, removeCallback),
                    Lists.newArrayList(registeredTypes.createParameter(Type.Simple.String.getId(),
                                    ChildUtil.logger(logger, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    1,
                                    1),
                            registeredTypes.createParameter(Type.Simple.String.getId(),
                                    ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    1,
                                    1)));
        }
    }

    public static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final Callback callback;
        private final RealUser.RemoveCallback<RealUserImpl> removeCallback;
        private final RealUserImpl.Factory userFactory;

        @Inject
        public Performer(@Assisted Logger logger,
                         @Assisted Callback callback,
                         @Assisted RealUser.RemoveCallback<RealUserImpl> removeCallback,
                         RealUserImpl.Factory userFactory) {
            this.logger = logger;
            this.callback = callback;
            this.userFactory = userFactory;
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
            RealUserImpl user = userFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addUser(user);
        }

        public interface Factory {
            Performer create(Logger logger,
                             Callback callback,
                             RealUser.RemoveCallback<RealUserImpl> removeCallback);
        }
    }
}
