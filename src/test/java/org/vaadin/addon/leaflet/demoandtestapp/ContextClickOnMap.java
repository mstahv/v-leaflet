package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.shared.MouseEventDetails;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletContextMenuEvent;
import org.vaadin.addon.leaflet.LeafletContextMenuListener;
import org.vaadin.addonhelpers.AbstractTest;

public class ContextClickOnMap extends AbstractTest {

    @Override
    public String getDescription() {
        return "Testing Context click events";
    }

    private LMap leafletMap;

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();
        final LOpenStreetMapLayer lOpenStreetMapLayer = new LOpenStreetMapLayer();

        leafletMap.addLayer(lOpenStreetMapLayer);

        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2);

        LPolygon polygon = new LPolygon(new Point(0, 0), new Point(30, 30), new Point(0, 30));

        leafletMap.addLayer(polygon);

        polygon.addContextMenuListener(new LeafletContextMenuListener() {
            @Override
            public void onContextMenu(LeafletContextMenuEvent event) {

                Notification.show("CxtClick at polygon at " + event.toString());

            }
        });

        polygon.addClickListener(new LeafletClickListener() {
            @Override
            public void onClick(LeafletClickEvent event) {
                Notification.show("Std Click at polygon at " + event.toString());
            }
        });

        // prevent bubbling of events to DOM parents(like the map)
        polygon.setBubblingMouseEvents(false);

        leafletMap.addContextMenuListener(new LeafletContextMenuListener() {
            @Override
            public void onContextMenu(LeafletContextMenuEvent event) {
                Point point = event.getPoint();

                LMarker marker = new LMarker(point);
                marker.setPopup("Created by ContextClick on lOpenStreetMapLayer");
                leafletMap.addComponent(marker);
                marker.openPopup();

            }
        });

        leafletMap.addClickListener(new LeafletClickListener() {
            @Override
            public void onClick(LeafletClickEvent event) {
                if (event.getMouseEvent().getButton() == MouseEventDetails.MouseButton.LEFT) {
                    Notification.show("Std Click on map at " + event.toString() + ". Use context click to add marker.");
                }
            }
        });

        return leafletMap;
    }
}
