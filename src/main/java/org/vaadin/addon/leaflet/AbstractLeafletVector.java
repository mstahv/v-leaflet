package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.AbstractLeafletVectorState;

public abstract class AbstractLeafletVector extends AbstractLeafletLayer {

    @Override
    protected AbstractLeafletVectorState getState() {
        return (AbstractLeafletVectorState) super.getState();
    }

    public void setColor(String color) {
        getState().color = color;
    }

    public void setStroke(Boolean stroke) {
        getState().stroke = stroke;
    }

    public void setFill(Boolean fill) {
        getState().fill = fill;
    }

    public void setFillColor(String fillColor) {
        getState().fillColor = fillColor;
    }

    public void setWeight(Integer weight) {
        getState().weight = weight;
    }

    public void setOpacity(Double opacity) {
        getState().opacity = opacity;
    }

    public void setDashArray(String dashArray) {
        getState().dashArray = dashArray;
    }

    public void setLineCap(String lineCap) {
        getState().lineCap = lineCap;
    }

    public void setLineJoin(String lineJoin) {
        getState().lineJoin = lineJoin;
    }

    public void setClickable(Boolean clickable) {
        getState().clickable = clickable;
    }
}