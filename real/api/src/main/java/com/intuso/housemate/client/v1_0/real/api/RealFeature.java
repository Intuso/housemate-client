package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.object.v1_0.api.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature extends Feature<
        RealList<RealCommand>,
        RealList<RealValue<?>>,
        RealFeature> {}
