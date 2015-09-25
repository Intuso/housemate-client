package com.intuso.housemate.client.v1_0.real.api.factory.user;

import com.intuso.housemate.client.v1_0.real.api.RealUser;
import com.intuso.housemate.comms.v1_0.api.payload.UserData;

/**
* Created by tomc on 20/03/15.
*/
public interface RealUserFactory {
    public RealUser create(UserData data, RealUserOwner owner);
}
