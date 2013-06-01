package org.vaadin.addon.leaflet.client.vaadin;

import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.vector.Polygon;
import org.discotools.gwt.leaflet.client.layers.vector.PolylineOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LPolygon.class)
public class LeafletPolygonConnector extends LeafletPolylineConnector {

    private Polygon marker;

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
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

        marker = new Polygon(latlngs, options);

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
}
