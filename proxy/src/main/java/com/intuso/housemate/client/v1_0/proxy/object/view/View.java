package com.intuso.housemate.client.v1_0.proxy.object.view;

import java.io.Serializable;

/**
 * Created by tomc on 19/06/17.
 */
public abstract class View<VIEW extends View<?>> implements Serializable {

    public enum Mode {
        ANCESTORS,
        CHILDREN,
        SELECTION
    }

    private Mode mode;

    public View() {
        this(Mode.SELECTION);
    }

    public View(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
