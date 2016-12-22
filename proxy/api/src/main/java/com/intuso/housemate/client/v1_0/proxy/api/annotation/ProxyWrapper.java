package com.intuso.housemate.client.v1_0.proxy.api.annotation;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public interface ProxyWrapper {
    <T> T build(Logger logger, ProxyObject<?, ?> object, Class<T> tClass, String prefix);
}
