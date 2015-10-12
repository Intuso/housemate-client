package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.FeatureLoadedListener;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeature;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base and container class for simple proxy features
 */
public abstract class SimpleProxyFeature implements ProxyFeature<SimpleProxyFeature, SimpleProxyDevice> {

    protected final SimpleProxyDevice device;

    public SimpleProxyFeature(SimpleProxyDevice device) {
        this.device = device;
    }

    public final SimpleProxyFeature getThis() {
        return this;
    }

    public void load(final FeatureLoadedListener<SimpleProxyDevice, SimpleProxyFeature> listener) {
        List<TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(List<String> errors) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void succeeded() {
                listener.loadFinished(device, SimpleProxyFeature.this);
            }
        }, treeInfos));
    }

    private TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new TreeLoadInfo(childName, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
        return new TreeLoadInfo(objectName, children);
    }

    public final static class PowerControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.PowerControl<SimpleProxyCommand> {

        public PowerControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get("on") : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get("off") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("on", "off");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet();
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class StatefulPowerControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulPowerControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPowerControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get("on") : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get("off") : null;
        }

        @Override
        public SimpleProxyValue getIsOnValue() {
            return device.getValues() != null ? device.getValues().get("is-on") : null;
        }

        @Override
        public boolean isOn() {
            SimpleProxyValue value = getIsOnValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("on", "pff");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("is-on");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class PlaybackControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.PlaybackControl<SimpleProxyCommand> {

        public PlaybackControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get("play") : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get("pause") : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get("stop") : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get("rewind") : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get("forward") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("play", "pause", "stop", "forward", "rewind");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet();
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class StatefulPlaybackControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulPlaybackControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPlaybackControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyValue getIsPlayingValue() {
            return device.getValues() != null ? device.getValues().get("is-playing") : null;
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
            return device.getCommands() != null ? device.getCommands().get("play") : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get("pause") : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get("stop") : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get("rewind") : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get("forward") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("play", "pause", "stop", "forward", "rewind");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("is-playing");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class VolumeControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.VolumeControl<SimpleProxyCommand> {

        public VolumeControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get("mute") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-up") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-down") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("volume-up", "volume-down");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet();
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class StatefulVolumeControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulVolumeControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulVolumeControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get("mute") : null;
        }

        @Override
        public SimpleProxyValue getCurrentVolumeValue() {
            return device.getValues() != null ? device.getValues().get("current-volume") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-up") : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-down") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("volume-up", "volume-down");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("current-volume");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }
}
