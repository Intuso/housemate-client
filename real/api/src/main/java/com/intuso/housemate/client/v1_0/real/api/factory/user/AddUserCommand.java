package com.intuso.housemate.client.v1_0.real.api.factory.user;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.impl.type.StringType;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddUserCommand extends RealCommand {

    private final static String NAME_PARAMETER_ID = "name";
    private final static String NAME_PARAMETER_NAME = "Name";
    private final static String NAME_PARAMETER_DESCRIPTION = "The name of the new user";
    private final static String DESCRIPTION_PARAMETER_ID = "description";
    private final static String DESCRIPTION_PARAMETER_NAME = "Description";
    private final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new user";

    private final RealUserOwner owner;
    private final RealUserFactory userFactory;

    @Inject
    protected AddUserCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                             RealUserFactory userFactory, @Assisted RealUserOwner owner) {
        super(log, listenersFactory,
                owner.getAddUserCommandDetails().getId(), owner.getAddUserCommandDetails().getName(), owner.getAddUserCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType));
        this.owner = owner;
        this.userFactory = userFactory;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        owner.addUser(userFactory.create(new UserData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner));
    }

    public interface Factory {
        public AddUserCommand create(RealUserOwner owner);
    }
}
