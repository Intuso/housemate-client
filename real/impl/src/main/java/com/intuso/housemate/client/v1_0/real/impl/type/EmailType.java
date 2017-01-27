package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 01/05/14
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
public class EmailType extends RealRegexType {

    public final static String ID = "email";
    public final static String NAME = "Email";
    public final static String DESCRIPTION = "Email address of the form <username>@<host>";
    public final static String REGEX = ".+@.+\\..+";

    /**
     * @param managedCollectionFactory
     */
    @Inject
    public EmailType(@Type Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, ID), ID, NAME, DESCRIPTION, REGEX, managedCollectionFactory);
    }
}
