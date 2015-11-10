package com.intuso.housemate.client.v1_0.proxy.simple;

/**
 * Base and container class for simple proxy features
 */
public abstract class SimpleProxyFeatureImpls {

    protected final SimpleProxyFeature feature;

    public SimpleProxyFeatureImpls(SimpleProxyFeature feature) {
        this.feature = feature;
    }

    public final SimpleProxyFeatureImpls getThis() {
        return this;
    }

    public final static class PowerControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.PowerControl<SimpleProxyCommand> {

        public PowerControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("on") : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("off") : null;
        }
    }

    public final static class StatefulPowerControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulPowerControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPowerControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("on") : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("off") : null;
        }

        @Override
        public SimpleProxyValue getIsOnValue() {
            return feature.getValues() != null ? feature.getValues().get("is-on") : null;
        }

        @Override
        public boolean isOn() {
            SimpleProxyValue value = getIsOnValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }
    }

    public final static class PlaybackControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.PlaybackControl<SimpleProxyCommand> {

        public PlaybackControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyCommand getPlayCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("play") : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("pause") : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("stop") : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("rewind") : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("forward") : null;
        }
    }

    public final static class StatefulPlaybackControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulPlaybackControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPlaybackControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyValue getIsPlayingValue() {
            return feature.getValues() != null ? feature.getValues().get("is-playing") : null;
        }

        @Override
        public boolean isPlaying() {
            SimpleProxyValue value = getIsPlayingValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }

        @Override
        public SimpleProxyCommand getPlayCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("play") : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("pause") : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("stop") : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("rewind") : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("forward") : null;
        }
    }

    public final static class VolumeControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.VolumeControl<SimpleProxyCommand> {

        public VolumeControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("mute") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-up") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-down") : null;
        }
    }

    public final static class StatefulVolumeControl
            extends SimpleProxyFeatureImpls
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulVolumeControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulVolumeControl(SimpleProxyFeature feature) {
            super(feature);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("mute") : null;
        }

        @Override
        public SimpleProxyValue getCurrentVolumeValue() {
            return feature.getValues() != null ? feature.getValues().get("current-volume") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-up") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-down") : null;
        }
    }
}
