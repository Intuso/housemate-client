package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.User;

public interface RealUser<COMMAND extends RealCommand<?, ?, ?>,
        EMAIL_PROPERTY extends RealProperty<String, ?, ?, ?>,
        USER extends RealUser<COMMAND, EMAIL_PROPERTY, USER>>
        extends User<COMMAND,
        COMMAND,
        EMAIL_PROPERTY,
        USER> {

    interface Container<USER extends RealUser<?, ?, ?>, USERS extends RealList<? extends USER, ?>> extends User.Container<USERS>, RemoveCallback<USER> {
        void addUser(USER user);
    }

    interface RemoveCallback<USER extends RealUser<?, ?, ?>> {
        void removeUser(USER user);
    }
}
