package com.intuso.housemate.client.v1_0.real.api;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.factory.user.RealUserOwner;
import com.intuso.housemate.client.v1_0.real.api.impl.type.Email;
import com.intuso.housemate.client.v1_0.real.api.impl.type.EmailType;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;
import com.intuso.housemate.object.v1_0.api.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.User;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealUser
        extends RealObject<UserData, HousemateData<?>, RealObject<?, ? ,?, ?>, User.Listener>
        implements User<RealCommand, RealProperty<Email>> {

    private final RealCommand remove;
    private final RealProperty<Email> emailProperty;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealUser(final Log log, ListenersFactory listenersFactory,
                    @Assisted UserData data, @Assisted final RealUserOwner owner) {
        super(log, listenersFactory, data);
        this.remove = new RealCommand(log, listenersFactory, UserData.REMOVE_ID, UserData.REMOVE_ID, "Remove the user", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                owner.removeUser(RealUser.this);
            }
        };
        this.emailProperty = new RealProperty<>(log, listenersFactory, UserData.EMAIL_ID, UserData.EMAIL_ID, "The user's email address", new EmailType(log, listenersFactory), (Email)null);
        addChild(remove);
        addChild(emailProperty);
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealProperty<Email> getEmailProperty() {
        return emailProperty;
    }

}
