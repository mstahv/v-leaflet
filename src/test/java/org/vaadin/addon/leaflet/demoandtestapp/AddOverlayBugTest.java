package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.ExternalResource;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LImageOverlay;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

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
        lmap.setCenter(40.712216, -74.22655);
        lmap.setWidth("500px");
        lmap.setHeight("400px");

        lmapContainer.addComponent(lmap);

        // base laser 1 & 2 (dummy base layers)
        LOpenStreetMapLayer osm1 = new LOpenStreetMapLayer();
        osm1.setActive(false);
        lmap.addBaseLayer(osm1, "Base Layer 1");
        LTileLayer osm2 = new LTileLayer("https://a.tile.thunderforest.com/cycle/{z}/{x}/{y}.png");
        osm2.setAttributionString("© OpenStreetMap contributors. Tiles courtesy of Andy Allan");
        
        osm2.setActive(true);
        lmap.addBaseLayer(osm2, "Base Layer 2");

        // BUG: after removing/adding the base layer and also existing wms layers are duplicated
        Button wmsLayerRemoveAddButton = new Button("Add");
        wmsLayerRemoveAddButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                ExternalResource url = new ExternalResource("https://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg");
                LImageOverlay imageOverlay = new LImageOverlay(url, new Bounds(new Point(40.712216, -74.22655), new Point(40.773941, -74.12544)));
                imageOverlay.setOpacity(0.5);
                imageOverlay.setAttribution("University of Texas");
                lmap.addOverlay(imageOverlay, "imagelayer-" + System.currentTimeMillis() );

                // dummy wms layer
                LWmsLayer result = new LWmsLayer();
                result.setFormat("image/png");
                result.setUrl("not/working/url/to/your/geoserver");
                result.setLayers("layerselection");

                // add new wms layer
                lmap.addOverlay(result, "layer-" + System.currentTimeMillis());
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
