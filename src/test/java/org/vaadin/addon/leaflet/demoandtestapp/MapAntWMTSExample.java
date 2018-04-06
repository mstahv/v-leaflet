package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Crs;
import org.vaadin.addonhelpers.AbstractTest;

public class MapAntWMTSExample extends AbstractTest {

    @Override
    public String getDescription() {
        return "An example fo WMTS layer.";
    }

    @Override
    public Component getTestComponent() {
        LMap leafletMap = new LMap();
        leafletMap.setCrs(Crs.EPSG3857);
        LTileLayer tileLayer = new LTileLayer("http://wmts.mapant.fi/wmts_EPSG3857.php?z={z}&x={x}&y={y}");
        tileLayer.setMaxZoom(19);
        tileLayer.setMinZoom(7);
        tileLayer.setAttributionString("<a href=\"http://www.maanmittauslaitos.fi/en/digituotteet/laser-scanning-data\" target=\"_blank\">Laser scanning</a> and <a href=\"http://www.maanmittauslaitos.fi/en/digituotteet/topographic-database\" target=\"_blank\">topographic</a> data provided by the <a href=\"http://www.maanmittauslaitos.fi/en\" target=\"_blank\">National Land Survey of Finland</a> under the <a href=\"https://creativecommons.org/licenses/by/4.0/legalcode\">Creative Commons license</a>.");
        
        leafletMap.addLayer(tileLayer);
        
        leafletMap.setCenter(61.0, 26);
        leafletMap.setZoomLevel(17);

        return leafletMap;
    }

}
