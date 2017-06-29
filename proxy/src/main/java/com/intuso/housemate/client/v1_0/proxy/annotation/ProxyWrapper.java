package com.intuso.housemate.client.v1_0.proxy.annotation;

import com.intuso.housemate.client.v1_0.proxy.object.ProxyObject;

/**
 * Created by tomc on 16/12/16.
 */
public interface ProxyWrapper {
    <T> T build(ProxyObject<?, ?, ?> object, Class<T> tClass, String prefix, long commandTimeout);
}
