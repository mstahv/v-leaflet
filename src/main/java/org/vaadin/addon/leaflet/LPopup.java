package org.vaadin.addon.leaflet;

import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.shared.LeafletPopupState;
import org.vaadin.addon.leaflet.shared.PopupServerRpc;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shared.PopupState;
import org.vaadin.addon.leaflet.util.JTSUtil;

import org.locationtech.jts.geom.Geometry;

/**
 * Standalone Popup to be displayed on the map.
 */
public class LPopup extends AbstractLeafletDivOverlay {

    @Override
    protected LeafletPopupState getState() {
        return (LeafletPopupState) super.getState();
    }

    public LPopup(double lat, double lon) {
    	this();
        getState().point = new Point(lat, lon);
    }

    public LPopup() {
        registerRpc(new PopupServerRpc() {

            @Override
            public void closed() {
            	close();
            }
        });
    }

    public LPopup(Point point) {
    	this();
        getState().point = point;
    }
    
    private LMap getMap() {
        return (LMap) getParent();
    }
    
    public LPopup(org.locationtech.jts.geom.Point jtsPoint) {
    	this(JTSUtil.toLeafletPoint(jtsPoint));
    }

    public LPopup setContent(String htmlContent) {
        return (LPopup) super.setContent(htmlContent);
    }

    public void close() {
        getMap().removeComponent(this);
    }

	@Override
	public Geometry getGeometry() {
		return JTSUtil.toPoint(getState().point);
	}

    public PopupState getPopupState() {
        return getState().popupState;
    }

    public void setPopupState(PopupState popupState){
        getState().popupState = popupState;
    }

    public void setCloseButton(boolean closeButtonVisible) {
        getState().popupState.closeButton = closeButtonVisible;
    }

    public void setCloseOnClick(boolean closeOnMapClick) {
        getState().popupState.closeOnClick	= closeOnMapClick;
    }

}
