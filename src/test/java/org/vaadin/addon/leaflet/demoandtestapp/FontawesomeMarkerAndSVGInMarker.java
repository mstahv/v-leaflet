package org.vaadin.addon.leaflet.demoandtestapp;


import com.vaadin.server.FontAwesome;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

public class FontawesomeMarkerAndSVGInMarker extends AbstractTest {

    @Override
    public String getDescription() {
        return "A should not show empty map";
    }


	@Override
	public Component getTestComponent() {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        // Getting my map.
        LMap map = new LMap();
        map.addComponent(new LOpenStreetMapLayer());

        LMarker lMarker = new LMarker(61, 22);
        lMarker.setIcon(FontAwesome.BEER);
        lMarker.setPopup("FontAwesome marker popup with anchor");
        lMarker.setPopupAnchor(new Point(0, -45));
        map.addComponent(lMarker);
        
        LCircleMarker lCircleMarker = new LCircleMarker(61,22, 2);
        map.addComponent(lCircleMarker);
        

        LMarker lMarker2 = new LMarker(62, 23);
        String svgCode = "<svg width=\"100\" height=\"100\">\n" +
"  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"green\" stroke-width=\"4\" fill=\"yellow\" />\n" +
"</svg>";
        lMarker2.setIconSize(new Point(100, 100));
        lMarker2.setIconAnchor(new Point(50, 50));
        lMarker2.setPopup("Custom svg popup with anchor");
        lMarker2.setPopupAnchor(new Point(0, -40));
        lMarker2.addStyleName("v-leaflet-custom-svg");
        lMarker2.setDivIcon(svgCode);
        lMarker2.addClickListener(new LeafletClickListener() {
            @Override
            public void onClick(LeafletClickEvent event) {
                Notification.show("Clicked " +  event.getPoint().toString());
            }
        });
        map.addComponent(lMarker2);
        
        LCircleMarker lCircleMarker2 = new LCircleMarker(62,23, 2);
        map.addComponent(lCircleMarker2);

        LMarker lMarker3 = new LMarker(60.1698560, 24.9383790);
        lMarker3.setIcon(FontAwesome.GOOGLE);
        lMarker3.setIconTextFill("#F00");
        lMarker3.setIconPathFill("#666");
        lMarker3.setIconPathStroke("#000");
        lMarker3.setPopup("Configurable FontAwesome marker popup with anchor");
        lMarker3.setPopupAnchor(new Point(0, -45));
        map.addComponent(lMarker3);

        map.zoomToContent();

        layout.addComponent(map);
        return layout;
	}
}
