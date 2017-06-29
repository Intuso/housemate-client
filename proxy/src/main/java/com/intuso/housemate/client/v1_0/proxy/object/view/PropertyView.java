package com.intuso.housemate.client.v1_0.proxy.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class PropertyView extends ValueBaseView<PropertyView> {

    private CommandView setCommandView;

    public PropertyView() {}

    public PropertyView(Mode mode) {
        super(mode);
    }

    public PropertyView(CommandView setCommandView) {
        super(Mode.SELECTION);
        this.setCommandView = setCommandView;
    }

    public CommandView getSetCommandView() {
        return setCommandView;
    }

    public PropertyView setSetCommandView(CommandView setCommandView) {
        this.setCommandView = setCommandView;
        return this;
    }
}
