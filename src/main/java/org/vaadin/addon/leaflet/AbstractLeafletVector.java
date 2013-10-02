package org.vaadin.addon.leaflet;

public abstract class AbstractLeafletVector extends AbstractLeafletLayer {

    public void setColor(String color) {
        getState().color = color;
    }

}