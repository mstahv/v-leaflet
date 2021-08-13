package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;

import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LWmsLayer;
import org.vaadin.addon.leaflet.LeafletLoadEvent;
import org.vaadin.addon.leaflet.LeafletLoadListener;
import org.vaadin.addon.leaflet.LeafletLoadingEvent;
import org.vaadin.addon.leaflet.LeafletLoadingListener;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

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

		LWmsLayer baseLayerIgn = new LWmsLayer();
		baseLayerIgn.setUrl("https://componentes.ign.es/wms-inspire/ign-base");
		baseLayerIgn.setLayers("SombreadoPenBal");
		baseLayerIgn.setTransparent(false);
		baseLayerIgn.setFormat("image/jpeg");
		baseLayerIgn.setOpacity(0.5);
		leafletMap.addBaseLayer(baseLayerIgn, "IGN");

		LTileLayer baseLayerOsm = new LTileLayer();
		baseLayerOsm.setUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
		leafletMap.addBaseLayer(baseLayerOsm, "OSM");

		LLayerGroup groupAreas = new LLayerGroup();

		LWmsLayer layerWmsAreas = new LWmsLayer();
		layerWmsAreas.setUrl("https://componentes.ign.es/wms-inspire/ign-base");
		layerWmsAreas.setLayers("NucleosPob_mayores,LugarInteres");
		layerWmsAreas.setTransparent(true);
		layerWmsAreas.setFormat("image/png");
		layerWmsAreas.setOpacity(0.5);
		groupAreas.addComponent(layerWmsAreas);

		LWmsLayer layerWmsWater = new LWmsLayer();
		layerWmsWater.setUrl("https://componentes.ign.es/wms-inspire/ign-base");
		layerWmsWater.setLayers("HY.PhysicalWaters.Waterbodies");
		layerWmsWater.setTransparent(true);
		layerWmsWater.setFormat("image/png");
		groupAreas.addComponent(layerWmsWater);

		LWmsLayer layerWmsStreets = new LWmsLayer();
		layerWmsStreets.setUrl("https://componentes.ign.es/wms-inspire/ign-base");
		layerWmsStreets
				.setLayers("Autopista,Autopista_Autovia,VialUrbano,CarreteraAutonomica,"
						+ "CarreteraConvencional,CarreteraNacional");
		layerWmsStreets.setTransparent(true);
		layerWmsStreets.setFormat("image/png");
		layerWmsStreets.setActive(false);

		LWmsLayer layerWmsAbiesAlbaGermany = new LWmsLayer();
		layerWmsAbiesAlbaGermany.setUrl("http://botanik4.botanik.uni-greifswald.de/geoserver/wms");
		layerWmsAbiesAlbaGermany.setLayers("indicia:indiciaGroup");
		layerWmsAbiesAlbaGermany.setTransparent(true);
		layerWmsAbiesAlbaGermany.setOpacity(.6);
		layerWmsAbiesAlbaGermany.setFormat("image/png");
		layerWmsAbiesAlbaGermany.setActive(true);
		// BOUNDS should be set to current view port, because of server side cluster at some scale level - but this is just for try viewparams
		layerWmsAbiesAlbaGermany.setViewparams("TAXONMEANINGID:274;BOUNDS:POLYGON(( 9.44617309618379 54.84370034122247\\,9.44617309618379 50.86696466779405\\,18.301153563701007 50.86696466779405\\,18.301153563701007 54.84370034122247\\,9.44617309618379 54.84370034122247))");

		layerWmsAbiesAlbaGermany.addLoadListener(new LeafletLoadListener()
		{
		   @Override
		   public void onLoad(LeafletLoadEvent event)
		   {
		      Notification.show("onLoad", Type.TRAY_NOTIFICATION);
		   }
		});

		layerWmsAbiesAlbaGermany.addLoadingListener(new LeafletLoadingListener()
		{
		   @Override
		   public void onLoading(LeafletLoadingEvent event)
		   {
		      Notification.show("onLoanding", Type.TRAY_NOTIFICATION);
		   }
		});

                LWmsLayer layerWmsAbiesAlbaGermanyMV = new LWmsLayer();
                layerWmsAbiesAlbaGermanyMV.setUrl(
                      "https://wms.test.infinitenature.org/geoserver/werbeo/wms?");
                layerWmsAbiesAlbaGermanyMV.setLayers("mv-occ");
                layerWmsAbiesAlbaGermanyMV.setTransparent(true);
                layerWmsAbiesAlbaGermanyMV.setOpacity(.6);
                layerWmsAbiesAlbaGermanyMV.setFormat("image/png");
                layerWmsAbiesAlbaGermanyMV.setActive(true);
                layerWmsAbiesAlbaGermanyMV.setStyles("polygon");
                layerWmsAbiesAlbaGermanyMV.setCQLFilter("taxon=54870");

    leafletMap.addOverlay(groupAreas,"Populated Areas & Water");
		leafletMap.addOverlay(layerWmsStreets, "Streets");
		leafletMap.addOverlay(layerWmsAbiesAlbaGermany, "Distribution of Abies Alba in Germany");
                leafletMap.addOverlay(layerWmsAbiesAlbaGermanyMV,
                      "Distribution of Abies Alba in Mecklenburg-Vorpommern");
		return leafletMap;
	}
}
