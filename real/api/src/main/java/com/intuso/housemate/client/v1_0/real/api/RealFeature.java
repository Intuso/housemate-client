package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.object.v1_0.api.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature extends Feature<
        RealList<RealCommand>,
        RealList<RealValue<?>>,
        RealFeature> {

    interface Factory {
        RealFeature create(FeatureData data);
    }
}