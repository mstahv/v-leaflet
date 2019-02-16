package org.vaadin.addon.leaflet;

import com.vaadin.server.Resource;
import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.LeafletImageOverlayState;

public class LImageOverlay extends AbstractLeafletLayer {

    @Override
    protected LeafletImageOverlayState getState() {
        return (LeafletImageOverlayState) super.getState();
    }

    public LImageOverlay(Resource url, Bounds bounds) {
        setResource("url", url);
        getState().bounds = bounds;
    }

    @Override
    public Geometry getGeometry() {
        Bounds b = ((LeafletImageOverlayState) getState(false)).bounds;
        return JTSUtil.toLineString(new Point[]{new Point(b.getNorthEastLat(), b.getNorthEastLon()), new Point(b.getSouthWestLat(), b.getSouthWestLon())});
    }

    /**
     * @param opacity 0.0-1.0
     */
    public void setOpacity(double opacity) {
        getState().opacity = opacity;
    }

    /**
     * @param alt text for the alt attribute of the image (useful for accessibility).
     */
    public void setAlt(String alt) {
        getState().alt = alt;
    }

    /**
     * Set if the image overlay will emit mouse events when clicked or hovered.
     * @param interactive default is false
     */
    public void setInteractive(Boolean interactive) {
        getState().interactive = interactive;
    }

    /**
     * Set the explicit zIndex of the tile layer.
     * @param zIndex number. Default is 1
     */
    public void setZIndex(Integer zIndex) {
        getState().zIndex = zIndex;
    }


    public void setAttribution(String attribution) {
        getState().attribution = attribution;
    }

}
