package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class MouseListenersTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test for mouse{Over|Out} events.<br/>Polygon should be red on hovering.";
    }

    @Override
    public Component getTestComponent() {
        LMap leafletMap = new LMap();

        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(0);

        final LPolygon lPolygon = new LPolygon(new Point(0, 360), new Point(0, 390), new Point(60, 370));
        leafletMap.addComponent(lPolygon);
        lPolygon.addMouseOverListener(new LeafletMouseOverListener() {
            @Override
            public void onMouseOver(LeafletMouseOverEvent event) {
                lPolygon.setColor("red");
                System.err.println("onMouseOver");
            }
        });
        lPolygon.addMouseOutListener(new LeafletMouseOutListener() {
            @Override
            public void onMouseOut(LeafletMouseOutEvent event) {
                lPolygon.setColor("blue");
                System.err.println("onMouseOut");
            }
        });
        
        LMarker lMarker = new LMarker(0, 30);
        lMarker.addMouseOverListener(new LeafletMouseOverListener() {
			
			@Override
			public void onMouseOver(LeafletMouseOverEvent event) {
				System.err.println("marker mouse over");
			}
		});
        lMarker.addMouseOutListener(new LeafletMouseOutListener() {
			
			@Override
			public void onMouseOut(LeafletMouseOutEvent event) {
				System.err.println("marker mouse out");
			}
		});
        leafletMap.addLayer(lMarker);
        
        

        return leafletMap;
    }

}
