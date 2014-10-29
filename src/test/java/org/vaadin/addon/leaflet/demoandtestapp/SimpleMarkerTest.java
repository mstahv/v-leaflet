package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;

public class SimpleMarkerTest extends AbstractTest {
    private LMarker lMarker;

	@Override
	public String getDescription() {
		return "Add marker test case";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
	    

		leafletMap = new LMap();

		LTileLayer pk = new LTileLayer();
		pk.setUrl("http://{s}.kartat.kapsi.fi/peruskartta/{z}/{x}/{y}.png");
		pk.setAttributionString("Maanmittauslaitos, hosted by kartat.kapsi.fi");
		pk.setMaxZoom(18);
		pk.setSubDomains("tile2");
		pk.setDetectRetina(true);
		leafletMap.addBaseLayer(pk, "Peruskartta");

		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);
        lMarker = new LMarker(60.4525, 22.301);
        lMarker.setPopup("Popupstring");
		leafletMap.addComponent(lMarker);
        
        lMarker.openPopup();
	    
		return leafletMap;

	}

	@Override
	protected void setup() {
		super.setup();
        content.addComponentAsFirst(new Button("Open popup", new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
                lMarker.setPopup("dadaa");
                lMarker.openPopup();
            }
        }));

	}
}
