package com.intuso.housemate.client.v1_0.proxy.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ValueBaseView<VIEW extends ValueBaseView<?>> extends View<VIEW> {

    public ValueBaseView() {}

    public ValueBaseView(Mode mode) {
        super(mode);
    }
}
