package com.intuso.housemate.client.v1_0.real.impl.annotation;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import org.slf4j.Logger;

import java.util.Set;

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
    public Set<String> findClasses(Logger logger, Object object) {
        return annotationParserV1_0.findClasses(logger, object);
    }

    @Override
    public Set<String> findAbilities(Logger logger, Object object) {
        return annotationParserV1_0.findAbilities(logger, object);
    }

    @Override
    public Iterable<RealCommandImpl> findCommands(Logger logger, String idPrefix, Object object) {
        return annotationParserV1_0.findCommands(logger, idPrefix, object);
    }

    @Override
    public Iterable<RealValueImpl<?>> findValues(Logger logger, String idPrefix, Object object) {
        return annotationParserV1_0.findValues(logger, idPrefix, object);
    }

    @Override
    public Iterable<RealPropertyImpl<?>> findProperties(Logger logger, String idPrefix, Object object) {
        return annotationParserV1_0.findProperties(logger, idPrefix, object);
    }
}
