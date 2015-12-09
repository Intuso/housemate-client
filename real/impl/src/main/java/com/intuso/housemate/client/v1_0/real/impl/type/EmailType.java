package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.type.Email;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 01/05/14
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
public class EmailType extends RealRegexType<Email> {

    public final static String ID = "email";
    public final static String NAME = "Email";

    /**
     * @param logger              the log
     * @param listenersFactory
     */
    @Inject
    public EmailType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory, ID, NAME, "Email address of the form <username>@<host>", 1, 1, ".+@.+\\..+");
    }

    @Override
    public TypeInstance serialise(Email email) {
        return email == null ? null : new TypeInstance(email.getEmail());
    }

    @Override
    public Email deserialise(TypeInstance instance) {
        return instance == null || instance.getValue() == null ? null : new Email(instance.getValue());
    }
}
