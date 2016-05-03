package com.intuso.housemate.client.v1_0.proxy.simple;

import com.intuso.housemate.client.v1_0.proxy.api.object.feature.ProxyFeature;
import com.intuso.housemate.client.v1_0.proxy.api.object.feature.ProxyFeatureFactory;

/**
 * Feature factory for simple proxy features
 */
public class SimpleProxyFeatureFactory extends ProxyFeatureFactory<SimpleProxyFeature, ProxyFeature> {

    @Override
    public SimpleProxyFeatureImpls.PowerControl getPowerControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.PowerControl(feature);
    }

    @Override
    public SimpleProxyFeatureImpls.StatefulPowerControl getStatefulPowerControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.StatefulPowerControl(feature);
    }

    @Override
    public SimpleProxyFeatureImpls.PlaybackControl getPlaybackControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.PlaybackControl(feature);
    }

    @Override
    public SimpleProxyFeatureImpls.StatefulPlaybackControl getStatefulPlaybackControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.StatefulPlaybackControl(feature);
    }

    @Override
    public SimpleProxyFeatureImpls.VolumeControl getVolumeControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.VolumeControl(feature);
    }

    @Override
    public SimpleProxyFeatureImpls.StatefulVolumeControl getStatefulVolumeControl(SimpleProxyFeature feature) {
        return new SimpleProxyFeatureImpls.StatefulVolumeControl(feature);
    }

    @Override
    protected <F extends ProxyFeature> F getUnknown(SimpleProxyFeature feature) {
        return null;
    }
}
