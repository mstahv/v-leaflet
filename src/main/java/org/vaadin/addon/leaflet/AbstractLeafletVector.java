package org.vaadin.addon.leaflet;

public abstract class AbstractLeafletVector extends LeafletLayer {

    public AbstractLeafletVector(String name) {
        super(name);
    }

    public void setColor(String color) {
        getState().color = color;
    }

}