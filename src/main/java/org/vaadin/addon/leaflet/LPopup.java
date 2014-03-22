package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.shared.AbstractComponentState;
import com.vividsolutions.jts.geom.Geometry;

import org.vaadin.addon.leaflet.client.LeafletPopupState;
import org.vaadin.addon.leaflet.client.PopupServerRpc;

/**
 * Standalone Popup to be displayed on the map.
 */
public class LPopup extends AbstractLeafletLayer {

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
    
    public LPopup(com.vividsolutions.jts.geom.Point jtsPoint) {
    	this(JTSUtil.toLeafletPoint(jtsPoint));
    }
    
    public LPopup setContent(String htmlContent) {
        getState().htmlContent = htmlContent;
        return this;
    }
    
    public String getContent() {
        return getState(false).htmlContent;
    }
    
    @Override
    protected LeafletPopupState getState(boolean markAsDirty) {
    	return (LeafletPopupState) super.getState(markAsDirty);
    }

    public void setPoint(Point p) {
        getState().point = p;
    }

    public Point getPoint() {
        return getState().point;
    }

    public void close() {
        getMap().removeComponent(this);
    }

	@Override
	public Geometry getGeometry() {
		return JTSUtil.toPoint(getState().point);
	}

	public void setCloseButton(boolean closeButtonVisible) {
		getState().closeButton = closeButtonVisible;
	}

	public void setCloseOnClick(boolean closeOnMapClick) {
		getState().closeOnClick	= closeOnMapClick;
	}


}
