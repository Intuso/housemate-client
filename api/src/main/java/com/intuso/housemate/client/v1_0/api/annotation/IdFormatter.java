package com.intuso.housemate.client.v1_0.api.annotation;

import java.util.Map;

/**
 * Created by tomc on 02/02/17.
 */
public interface IdFormatter {
    String format(Map<String, Object> args);
}
