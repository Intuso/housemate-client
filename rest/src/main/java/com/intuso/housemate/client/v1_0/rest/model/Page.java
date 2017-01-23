package com.intuso.housemate.client.v1_0.rest.model;

import java.util.List;

/**
 * Created by tomc on 23/01/17.
 */
public class Page<T> {

    private int offset;
    private int total;
    private List<T> elements;

    public Page() {}

    public Page(int offset, int total, List<T> elements) {
        this.offset = offset;
        this.total = total;
        this.elements = elements;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }
}
