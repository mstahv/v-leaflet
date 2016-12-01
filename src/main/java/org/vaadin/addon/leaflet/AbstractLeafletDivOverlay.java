package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.AbstractLeafletDivOverlayState;
import org.vaadin.addon.leaflet.shared.Point;

public abstract class AbstractLeafletDivOverlay extends AbstractLeafletLayer {

    @Override
    protected AbstractLeafletDivOverlayState getState() {
        return (AbstractLeafletDivOverlayState) super.getState();
    }

    @Override
    protected AbstractLeafletDivOverlayState getState(boolean markAsDirty) {
        return (AbstractLeafletDivOverlayState) super.getState(markAsDirty);
    }

    public void setPoint(Point p) {
        getState().point = p;
    }

    public Point getPoint() {
        return getState().point;
    }

    public AbstractLeafletDivOverlay setContent(String htmlContent) {
        getState().htmlContent = htmlContent;
        return this;
    }

    public String getContent() {
        return getState(false).htmlContent;
    }
}
