package com.intuso.housemate.client.v1_0.api.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceConnectedView extends DeviceView<DeviceConnectedView> {

    public DeviceConnectedView() {}

    public DeviceConnectedView(Mode mode) {
        super(mode);
    }

    public DeviceConnectedView(CommandView renameCommandView, ListView<DeviceComponentView> componentsView) {
        super(renameCommandView, componentsView);
    }
}
