package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.type.Email;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;
import com.intuso.housemate.object.v1_0.api.User;

public interface RealUser
        extends User<RealCommand, RealProperty<Email>, RealUser> {

    interface Container extends User.Container<RealList<RealUser>>, RemoveCallback {
        void addUser(RealUser user);
    }

    interface RemoveCallback {
        void removeUser(RealUser user);
    }

    interface Factory {
        RealUser create(UserData data, RemoveCallback removeCallback);
    }
}
