package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMarkerClientRpc;
import org.vaadin.addon.leaflet.client.vaadin.LeafletMarkerState;
import org.vaadin.addon.leaflet.client.vaadin.PopupState;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Prototype. This might be technically easier to implement as an extension with
 * Leaflet, but I'm trying this way to pave way for OL integration (which has
 * vectorlayer, extensions cannot have children -> needs to be
 * componentcontainer)
 * 
 */
public class LMarker extends LeafletLayer {

    @Override
    protected LeafletMarkerState getState() {
        return (LeafletMarkerState) super.getState();
    }

    public LMarker(double lat, double lon) {
        super(null);
        getState().point = new Point(lat, lon);
    }

    public LMarker() {
        super(null);
    }

    public LMarker(Point point) {
        super(null);
        getState().point = point;
    }

    public void setPoint(Point p) {
        getState().point = p;
    }

    public Point getPoint() {
        return getState().point;
    }

    public void setIconSize(Point point) {
        getState().iconSize = point;
    }

    public void setIconAnchor(Point point) {
        getState().iconAnchor = point;
    }

    public void setTitle(String title) {
        getState().title = title;
    }

    public void setDivIcon(String divIcon) {
        getState().divIcon = divIcon;
    }

    public void setPopup(String popup) {
        getState().popup = popup;
    }

    public void setPopupState(PopupState popupState){
        getState().popupState = popupState;
    }

    public void openPopup() {
        getRpcProxy(LeafletMarkerClientRpc.class).openPopup();
    }

    public void closePopup() {
        getRpcProxy(LeafletMarkerClientRpc.class).closePopup();
    }
}
