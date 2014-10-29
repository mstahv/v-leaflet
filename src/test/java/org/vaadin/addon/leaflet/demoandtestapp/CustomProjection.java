package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addon.leaflet.shared.Crs;
import org.vaadin.addonhelpers.AbstractTest;

public class CustomProjection extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test for using custom/explicit projection. Ensure the "
                + "WMS layer uses the projection given to map.";
    }

    @Override
    public Component getTestComponent() {
        LMap leafletMap = new LMap();

        LWmsLayer lWmsLayer = new LWmsLayer();
        lWmsLayer.setUrl("http://osm.omniscale.net/proxy/service");
        lWmsLayer.setLayers("osm");
        lWmsLayer.setFormat("image/png");
        // Toggle this line to see if WMS layer is requested with differen
        // CRS
        lWmsLayer.setCrs(Crs.EPSG4326);
        leafletMap.addLayer(lWmsLayer);

        leafletMap.setCenter(52.51739, 13.40209);
        leafletMap.setZoomLevel(14);
        // Toggle this line to see if WMS layer is requested with differen
        // CRS
//        leafletMap.setCrs(Crs.EPSG4326);

        return leafletMap;
    }

}
