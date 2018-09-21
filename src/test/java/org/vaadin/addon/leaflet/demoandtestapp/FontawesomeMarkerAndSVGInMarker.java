package org.vaadin.addon.leaflet.demoandtestapp;


import com.vaadin.server.ExternalResource;
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

    String svgMarker = "<svg version='1.1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' x='0px' y='0px' width='512px' height='512px' viewBox='0 0 512 512' style='enable-background:new 0 0 512 512;'><g><path fill='FILLCOLOR' d='M256,0c-70.703,0-128,57.313-128,128c0,51.5,30.563,95.563,74.375,115.875L256,512l53.625-268.125 C353.438,223.563,384,179.5,384,128C384,57.313,326.688,0,256,0z M256,192c-35.344,0-64-28.656-64-64s28.656-64,64-64 s64,28.656,64,64S291.344,192,256,192z'/> </g> </svg>";

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
        lMarker.setPopup("FontAwesome marker popup with anchor beer");
        lMarker.setPopupAnchor(new Point(0, -45));
        lMarker.addStyleName("beer");
        map.addComponent(lMarker);
        
        LCircleMarker lCircleMarker = new LCircleMarker(61,22, 2);
        map.addComponent(lCircleMarker);

        getPage().getStyles().add(".v-leaflet-custom-svg circle {stroke: blue;}");

        LMarker lMarker2 = new LMarker(62, 23);
        String svgCode = "<svg width=\"100\" height=\"100\">\n" +
"  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke-width=\"4\" fill=\"yellow\" />\n" +
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

        LMarker characterAsIcon = new LMarker(60, 21);
        characterAsIcon.setIcon("A1");
        map.addComponent(characterAsIcon);


        LMarker svgDataUrlMarker = new LMarker(62, 24);
        // Note that styling is not easy this way as css don't hook into images
        svgDataUrlMarker.addStyleName("red");
        svgDataUrlMarker.setIcon(new ExternalResource("data:image/svg+xml;utf8," + svgMarker.replace("FILLCOLOR", "red")));
        svgDataUrlMarker.setIconSize(new Point(50, 50));
        svgDataUrlMarker.setIconAnchor(new Point(25, 50));

        map.addComponent(svgDataUrlMarker);


        map.zoomToContent();

        layout.addComponent(map);
        return layout;
	}
}
