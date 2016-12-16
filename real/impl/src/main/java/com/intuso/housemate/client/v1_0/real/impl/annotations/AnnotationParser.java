package com.intuso.housemate.client.v1_0.real.impl.annotations;

import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public interface AnnotationParser {
    Iterable<RealCommandImpl> findCommands(Logger logger, String idPrefix, Object object);
    Iterable<RealValueImpl<?>> findValues(Logger logger, String idPrefix, Object object);
    Iterable<RealPropertyImpl<?>> findProperties(Logger logger, String idPrefix, Object object);
}
