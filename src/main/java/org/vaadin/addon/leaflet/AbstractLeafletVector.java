package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.AbstractLeafletVectorState;
import org.vaadin.addon.leaflet.shared.VectorStyle;

public abstract class AbstractLeafletVector extends AbstractLeafletLayer {

    @Override
    protected AbstractLeafletVectorState getState() {
        return (AbstractLeafletVectorState) super.getState();
    }
    
    public VectorStyle getStyle() {
    	return getState().getVectorStyle();
    }
    
    public void setStyle(VectorStyle style) {
    	getState().setVectorStyle(style);
    }

    public void setColor(String color) {
        getState().getVectorStyle().setColor(color);
    }

    public void setStroke(Boolean stroke) {
        getState().getVectorStyle().setStroke(stroke);
    }

    public void setFill(Boolean fill) {
        getState().getVectorStyle().setFill(fill);
    }

    public void setFillColor(String fillColor) {
        getState().getVectorStyle().setFillColor(fillColor);
    }

    public void setWeight(Integer weight) {
        getState().getVectorStyle().setWeight(weight);
    }

    public void setOpacity(Double opacity) {
        getState().getVectorStyle().setOpacity(opacity);
    }
    
    public void setFillOpacity(Double opacity) {
        getState().getVectorStyle().setFillOpacity(opacity);
    }

    public void setDashArray(String dashArray) {
        getState().getVectorStyle().setDashArray(dashArray);
    }

    public void setLineCap(String lineCap) {
        getState().getVectorStyle().setLineCap(lineCap);
    }

    public void setLineJoin(String lineJoin) {
        getState().getVectorStyle().setLineJoin(lineJoin);
    }

    public void setClickable(Boolean clickable) {
        getState().clickable = clickable;
    }
}