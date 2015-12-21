package com.intuso.housemate.client.v1_0.real.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 17/12/15.
 */
public class LoggerUtil {
    public static Logger child(Logger parent, String ... childNames) {
        StringBuilder loggerName = new StringBuilder(parent.getName());
        for(String childName : childNames)
            loggerName.append(".").append(childName);
        return LoggerFactory.getLogger(loggerName.toString());
    }
}
