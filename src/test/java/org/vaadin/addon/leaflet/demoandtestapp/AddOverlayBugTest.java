package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddOverlayBugTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test Bug with duplicate Layers in overlays";
	}

	private LMap lmap;

	@Override
	public Component getTestComponent() {
		
		VerticalLayout lmapContainer = new VerticalLayout();
	    lmapContainer.setMargin(true);
	    // create leaflet map
	    lmap = new LMap();
	    lmap.setCenter(40, 8);
	    lmap.setWidth("500px");
	    lmap.setHeight("400px");

	    lmapContainer.addComponent(lmap);

	    // base laser 1 & 2 (dummy base layers)
	    LOpenStreetMapLayer osm1 = new LOpenStreetMapLayer();
	    osm1.setActive(false);
	    lmap.addBaseLayer(osm1, "Base Layer 1");
	    LOpenStreetMapLayer osm2 = new LOpenStreetMapLayer();
	    osm2.setActive(true);
	    lmap.addBaseLayer(osm2, "Base Layer 2");

	    // BUG: after removing/adding the base layer and also existing wms layers are duplicated
	    Button wmsLayerRemoveAddButton = new Button("Add");
	    wmsLayerRemoveAddButton.addClickListener(new Button.ClickListener() {
	        @Override
	        public void buttonClick(ClickEvent event) {
	        	// dummy wms layer
	            LWmsLayer result = new LWmsLayer();
	            result.setFormat("image/png");
	            result.setUrl("not/working/url/to/your/geoserver");
	            result.setLayers("layerselection");

	            // add new wms layer
	            lmap.addOverlay(result, "layer-"+System.currentTimeMillis());
	        }
	    });
	    lmapContainer.addComponent(wmsLayerRemoveAddButton);

		return lmapContainer;
	}

	@Override
	protected void setup() {
		super.setup();
	}
}
