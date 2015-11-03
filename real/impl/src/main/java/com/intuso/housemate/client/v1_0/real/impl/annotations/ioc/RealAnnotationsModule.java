package com.intuso.housemate.client.v1_0.real.impl.annotations.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationProcessor;

/**
 * Created by tomc on 20/03/15.
 */
public class RealAnnotationsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnotationProcessor.class).in(Scopes.SINGLETON);
    }
}
