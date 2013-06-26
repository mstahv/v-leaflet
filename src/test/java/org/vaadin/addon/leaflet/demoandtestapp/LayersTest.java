package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;

import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayerWms;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Control;

import com.vaadin.ui.Component;

public class LayersTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test for various layer features";
	}

	private LMap leafletMap;

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(40.525282, -3.81603);
		leafletMap.setZoomLevel(11);

		leafletMap.setControls(new ArrayList<Control>(Arrays.asList(Control
				.values())));

		BaseLayer baseLayerIgn = new BaseLayer();
		baseLayerIgn.setWms(true);
		baseLayerIgn.setName("IGN");
		baseLayerIgn.setUrl("http://www.01.ign.es/wms-inspire/ign-base");
		baseLayerIgn.setLayers("SombreadoPenBal");
		baseLayerIgn.setTransparent(false);
		baseLayerIgn.setFormat("image/jpeg");
		baseLayerIgn.setOpacity(0.5);

		BaseLayer baseLayerOsm = new BaseLayer();
		baseLayerOsm.setName("OSM");
		baseLayerOsm.setUrl("http://{s}.tile.osm.org/{z}/{x}/{y}.png");

		LLayerGroup groupAreas = new LLayerGroup("Populated Areas & Water");

		LTileLayerWms layerWmsAreas = new LTileLayerWms(null);
		layerWmsAreas.setUrl("http://www.01.ign.es/wms-inspire/ign-base");
		layerWmsAreas.setLayers("NucleosPob_mayores,LugarInteres");
		layerWmsAreas.setTransparent(true);
		layerWmsAreas.setFormat("image/png");
		layerWmsAreas.setOpacity(0.5);
		groupAreas.addComponent(layerWmsAreas);

		LTileLayerWms layerWmsWater = new LTileLayerWms(null);
		layerWmsWater.setUrl("http://www.01.ign.es/wms-inspire/ign-base");
		layerWmsWater.setLayers("HY.PhysicalWaters.Waterbodies");
		layerWmsWater.setTransparent(true);
		layerWmsWater.setFormat("image/png");
		groupAreas.addComponent(layerWmsWater);

		LTileLayerWms layerWmsStreets = new LTileLayerWms("Streets");
		layerWmsStreets.setUrl("http://www.01.ign.es/wms-inspire/ign-base");
		layerWmsStreets
				.setLayers("Autopista,Autopista_Autovia,VialUrbano,CarreteraAutonomica,"
						+ "CarreteraConvencional,CarreteraNacional");
		layerWmsStreets.setTransparent(true);
		layerWmsStreets.setFormat("image/png");
		layerWmsStreets.setActive(false);

		leafletMap.addComponent(groupAreas);
		leafletMap.addComponent(layerWmsStreets);

		leafletMap.setBaseLayers(baseLayerOsm, baseLayerIgn);

		return leafletMap;
	}
}
