package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.real.api.type.Email;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;
import com.intuso.housemate.object.v1_0.api.User;

public interface RealUser
        extends User<RealCommand, RealProperty<Email>, RealUser> {

    interface RemovedListener {
        void userRemoved(RealUser user);
    }

    interface Factory {
        RealUser create(UserData data, RemovedListener removedListener);
    }
}
