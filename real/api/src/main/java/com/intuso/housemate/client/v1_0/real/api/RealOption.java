package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.client.v1_0.api.object.Option;

public interface RealOption<SUB_TYPES extends RealList<? extends RealSubType<?, ?, ?>, ?>,
        OPTION extends RealOption<SUB_TYPES, OPTION>>
        extends Option<SUB_TYPES,
        OPTION> {}
