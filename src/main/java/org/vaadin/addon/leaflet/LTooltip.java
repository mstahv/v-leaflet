package org.vaadin.addon.leaflet;

import org.locationtech.jts.geom.Geometry;
import org.vaadin.addon.leaflet.shared.LeafletTooltipState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shared.TooltipState;
import org.vaadin.addon.leaflet.util.JTSUtil;

/**
 * Standalone Tooltip to be displayed on the map.
 */
public class LTooltip extends AbstractLeafletDivOverlay {

    @Override
    protected LeafletTooltipState getState() {
        return (LeafletTooltipState) super.getState();
    }

    public LTooltip() {
    }

    public LTooltip(double lat, double lon) {
        getState().point = new Point(lat, lon);
    }

    public LTooltip(Point point) {
        getState().point = point;
    }

    private LMap getMap() {
        return (LMap) getParent();
    }

    public LTooltip(org.locationtech.jts.geom.Point jtsPoint) {
        this(JTSUtil.toLeafletPoint(jtsPoint));
    }

    @Override
    public LTooltip setContent(String htmlContent) {
        return (LTooltip) super.setContent(htmlContent);
    }

    public void close() {
        getMap().removeComponent(this);
    }

    @Override
    public Geometry getGeometry() {
        return JTSUtil.toPoint(getState().point);
    }

    public TooltipState getTooltipState() {
        return getState().tooltipState;
    }

    public void setTooltipState(TooltipState tooltipState) {
        getState().tooltipState = tooltipState;
    }

    public void setPane(String pane) {
        getState().tooltipState.pane = pane;
    }

    public void setOffset(Point p) {
        getState().tooltipState.offset = p;
    }

    public void setDirection(String direction) {
        getState().tooltipState.direction = direction;
    }

    public void setPermanent (boolean permanent) {
        getState().tooltipState.permanent = permanent;
    }

    public void setSticky(boolean sticky) {
        getState().tooltipState.sticky = sticky;
    }

    public void setInteractive(boolean interactive) {
        getState().tooltipState.interactive = interactive;
    }

    public void setOpacity(double opacity) {
        getState().tooltipState.opacity = opacity;
    }

}
