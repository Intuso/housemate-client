package com.intuso.housemate.client.v1_0.proxy.api.annotation;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;

/**
 * Created by tomc on 16/12/16.
 */
public interface AnnotationParser {
    <T> T build(ProxyObject<?, ?> object, Class<T> tClass);
}
