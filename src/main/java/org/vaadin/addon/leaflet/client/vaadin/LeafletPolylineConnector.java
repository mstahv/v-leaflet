package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.vector.Polyline;
import org.discotools.gwt.leaflet.client.layers.vector.PolylineOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LPolyline.class)
public class LeafletPolylineConnector extends
        AbstractLeafletLayerConnector<PolylineOptions> {

    private Polyline marker;

    @Override
    public LeafletPolylineState getState() {
        return (LeafletPolylineState) super.getState();
    }

    @Override
    protected PolylineOptions createOptions() {
        PolylineOptions o = new PolylineOptions();
        LeafletPolylineState s = getState();
        if (s.color != null) {
            o.setColor(s.color);
        }
        if (s.fill != null) {
            o.setFill(s.fill);
        }
        if (s.fillColor != null) {
            o.setFillColor(s.fillColor);
        }
        return o;
    }

    @Override
    protected void update() {
        if (marker != null) {
            removeFromParent(marker);
            EventHandlerManager.clearEventHandler(marker, Events.click);
        }
        if (getState().points == null) {
            return;
        }

        LatLng[] latlngs = new LatLng[getState().points.length];
        for (int i = 0; i < latlngs.length; i++) {
            Point p = getState().points[i];
            latlngs[i] = new LatLng(p.getLat(), p.getLon());
        }
        PolylineOptions options = createOptions();

        marker = new Polyline(latlngs, options);

        addToParent(marker);

        EventHandler<?> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rpc.onClick(new Point(event.getLatLng().lat(), event
                        .getLatLng().lng()));
            }
        };

        EventHandlerManager.addEventHandler(marker, Events.click, handler);
    }

    @Override
    public ILayer getLayer() {
        return marker;
    }

}
