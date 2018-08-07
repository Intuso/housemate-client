package com.intuso.housemate.client.v1_0.real.impl.annotation;

import com.intuso.housemate.client.v1_0.real.impl.RealCommandImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealDeviceComponentImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealPropertyImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealValueImpl;
import org.slf4j.Logger;

/**
 * Created by tomc on 16/12/16.
 */
public interface AnnotationParser {
    Iterable<RealDeviceComponentImpl> findDeviceComponents(Logger logger, Object object);
    Iterable<RealCommandImpl> findCommands(Logger logger, Object object);
    Iterable<RealValueImpl<?>> findValues(Logger logger, Object object);
    Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object);
}
