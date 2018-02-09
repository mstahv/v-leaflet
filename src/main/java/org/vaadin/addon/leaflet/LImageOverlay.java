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

    public void setAttribution(String attribution) {
        getState().attribution = attribution;
    }

}
