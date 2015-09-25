package com.intuso.housemate.client.v1_0.proxy.simple;

import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeatureFactory;

/**
 * Feature factory for simple proxy features
 */
public class SimpleProxyFeatureFactory
        extends ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyDevice> {

    @Override
    public SimpleProxyFeature.PowerControl getPowerControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.PowerControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulPowerControl getStatefulPowerControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.StatefulPowerControl(device);
    }

    @Override
    public SimpleProxyFeature.PlaybackControl getPlaybackControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.PlaybackControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulPlaybackControl getStatefulPlaybackControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.StatefulPlaybackControl(device);
    }

    @Override
    public SimpleProxyFeature.VolumeControl getVolumeControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.VolumeControl(device);
    }

    @Override
    public SimpleProxyFeature.StatefulVolumeControl getStatefulVolumeControl(SimpleProxyDevice device) {
        return new SimpleProxyFeature.StatefulVolumeControl(device);
    }
}
