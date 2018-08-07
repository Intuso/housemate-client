package com.intuso.housemate.client.v1_0.real.impl.annotation;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealDeviceComponentImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public class AnnotationParserImpl implements AnnotationParser {

    private final AnnotationParserV1_0 annotationParserV1_0;

    @Inject
    public AnnotationParserImpl(AnnotationParserV1_0 annotationParserV1_0) {
        this.annotationParserV1_0 = annotationParserV1_0;
    }

    @Override
    public Iterable<RealDeviceComponentImpl> findDeviceComponents(Logger logger, Object object) {
        return annotationParserV1_0.findDeviceComponents(logger, object);
    }

    @Override
    public Iterable<RealCommandImpl> findCommands(Logger logger, Object object) {
        return annotationParserV1_0.findCommands(logger, object);
    }

    @Override
    public Iterable<RealValueImpl<?>> findValues(Logger logger, Object object) {
        return annotationParserV1_0.findValues(logger, object);
    }

    @Override
    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object) {
        return annotationParserV1_0.findProperties(logger, object);
    }
}
