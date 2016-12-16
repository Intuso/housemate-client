package com.intuso.housemate.client.v1_0.real.impl.annotations.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationParser;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationParserImpl;
import com.intuso.housemate.client.v1_0.real.impl.annotations.AnnotationParserV1_0;

/**
 * Created by tomc on 20/03/15.
 */
public class AnnotationParserV1_0Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnotationParser.class).to(AnnotationParserImpl.class);
        bind(AnnotationParserV1_0.class).in(Scopes.SINGLETON);
   }
}
