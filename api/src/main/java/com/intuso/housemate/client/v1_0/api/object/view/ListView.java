package com.intuso.housemate.client.v1_0.api.object.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tomc on 19/06/17.
 */
public class ListView<CHILD_VIEW extends View> extends View {

    private CHILD_VIEW view;
    private Set<String> elements = new HashSet<>();

    public ListView() {}

    public ListView(Mode mode) {
        super(mode);
    }

    public ListView(CHILD_VIEW view) {
        super(Mode.CHILDREN);
        this.view = view;
    }

    public ListView(CHILD_VIEW view, String... elements) {
        this(view, new HashSet<>(Arrays.asList(elements)));
    }

    public ListView(CHILD_VIEW view, Set<String> elements) {
        super(Mode.SELECTION);
        this.view = view;
        this.elements = elements;
    }

    public CHILD_VIEW getView() {
        return view;
    }

    public ListView<CHILD_VIEW> setView(CHILD_VIEW view) {
        this.view = view;
        return this;
    }

    public Set<String> getElements() {
        return elements;
    }

    public ListView<CHILD_VIEW> setElements(Set<String> elements) {
        this.elements = elements;
        return this;
    }

    public ListView<CHILD_VIEW> addElement(String... elements) {
        if(this.elements == null)
            this.elements = new HashSet<>();
        this.elements.addAll(new HashSet<>(Arrays.asList(elements)));
        return this;
    }
}
