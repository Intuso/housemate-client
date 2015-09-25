package com.intuso.housemate.client.v1_0.proxy.api;

import com.intuso.housemate.object.v1_0.api.Failable;
import com.intuso.housemate.object.v1_0.api.Value;

/**
 * Created by tomc on 16/09/15.
 */
public interface ProxyFailable<ERROR_VALUE extends Value<?, ?>> extends Failable<ERROR_VALUE> {

    String getError();
}
