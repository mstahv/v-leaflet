package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.communication.ClientRpc;

public interface LeafletMapClientRpc extends ClientRpc {

	void setCenter(Double lat, Double lon, Double zoom);
    void flyTo(Double lat, Double lon, Double zoom);
	void zoomToExtent(Bounds bounds);
    void setMaxBounds(Bounds bounds);
    void setDragging(boolean dragging);
    void setTouchZoom(boolean touchZoome);
    void setDoubleClickZoom(boolean doubleClickZoom);
    void setBoxZoom(boolean boxZoom);
    void setScrollWheelZoom(boolean scrollWheelZoom);
    void setKeyboard(boolean keyboard);
    void locate(boolean watch, boolean highaccuracy, boolean updateView);
    void stopLocate();
    public void translate(int x, int y);

    void getSize();


}
