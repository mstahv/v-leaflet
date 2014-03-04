package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.control.LAttribution;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class HasControlTest extends AbstractTest {

	String attrUSGS = "Tiles courtesy of U.S. Geological Survey";

	private LMap map;
	private LTileLayer currentBaseMap;
	
	private List<LayerWrapper> baseLayers;
	private List<LayerWrapper> overLays;
	
	private int overlayZindex = 100;
	@Override
	public String getDescription() {
		return "Add/remove layers without LLayers (tests hasControl method)";
	}

	@Override
	public Component getTestComponent() {
		
		map = new LMap();
		map.setCenter(40, -105.2);
		map.setZoomLevel(11);
		map.addControl(new LScale());
		
		return map;
	}

	
	@Override
	protected void setup() {
		super.setup();
		
		Label l = new Label("This test adds and removes overlay layers using addLayers and removeLayers.<br/> " +
				"A Leaflet Layers Control should never appear.", 
				ContentMode.HTML);
		l.setWidth("500px");
		
		HorizontalLayout hl = new HorizontalLayout();
		CheckBox useLLayers = new CheckBox("Use LLayers control", false);
		useLLayers.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				CheckBox cb = (CheckBox) event.getProperty();
				boolean checked = cb.getValue();
				if (checked) {
					LLayers lc = new LLayers();
					map.addControl(lc);
					for (LayerWrapper lw : baseLayers) {
						lc.addBaseLayer(lw.getLayer(), lw.getDescription());
					}
					for (LayerWrapper lw : overLays) {
						lc.addOverlay(lw.getLayer(), lw.getDescription());
					}
				} else {
					map.removeControl(map.getLayersControl());
				}
			}
		});
		hl.addComponents(l,setupBaseMaps(), setupOverlays(), useLLayers);
		
		content.addComponentAsFirst(hl);
	}

	private Component setupOverlays() {
		// These are actually basemaps, but we'll dial down the opacity to use as overlays.
		LayerWrapper shl = createOverlay(
				"http://basemap.nationalmap.gov/ArcGIS/rest/services/USGSShadedReliefOnly/MapServer/tile/{z}/{y}/{x}",
				"USGS Shaded Relief "
		);
		
		LayerWrapper cl = createOverlay(
				"http://basemap.nationalmap.gov/ArcGIS/rest/services/TNM_Contours/MapServer/tile/{z}/{y}/{x}",
				"USGS Contours"
		);
		
		overLays = Arrays.asList(shl, cl);
		
		CheckBox sh = createOverLayCheckBox(shl);
		CheckBox c = createOverLayCheckBox(cl);
		
		VerticalLayout vl = new VerticalLayout();
		vl.addComponents(sh, c);
		vl.setSizeUndefined();
	
		Panel p = new Panel();
		p.setCaption("Overlay maps");
		p.setContent(vl);
		p.setSizeUndefined();
		
		map.addLayer(shl.getLayer());
		map.addLayer(cl.getLayer());
		//map.addOverlay(shl.getLayer(), shl.getDescription());
		//map.addOverlay(cl.getLayer(), cl.getDescription());
		return p;
	}

	private CheckBox createOverLayCheckBox(LayerWrapper lw) {
		CheckBox c = new CheckBox(lw.getDescription(), false);
		c.setImmediate(true);
		c.setData(lw);
		c.addValueChangeListener(ovListener);
		
		return c;
	}
	
	private ValueChangeListener ovListener = new ValueChangeListener() {
		
		@Override
		public void valueChange(ValueChangeEvent event) {
			String hasControls = "has LLayers? " + map.hasControl(LLayers.class) +
					"\nhas Scale? " + map.hasControl(LScale.class);
			Notification.show(hasControls);
			CheckBox cb = (CheckBox) event.getProperty();
			boolean checked = cb.getValue();
			LayerWrapper lw = (LayerWrapper) cb.getData();
			if (checked) {
				lw.getLayer().setActive(true);
				map.addLayer(lw.getLayer());
				
			} else {
				lw.getLayer().setActive(false);
				map.removeLayer(lw.getLayer());
			}
			//lw.getLayer().setActive(checked);
			trayNotify("Layer " + cb.getCaption() + " should now be " + (checked ? "" : "not") + " visible");
		}
	};
	
	private LayerWrapper createOverlay(String url, String desc) {
		Double opacity = 0.6;
		
		LTileLayer l = new LTileLayer();
		l.setUrl(url);
		l.setAttributionString(attrUSGS);
		l.setOpacity(opacity);
		l.setActive(false);
		l.setZindex(overlayZindex++);
		return new LayerWrapper(desc, l);
	}
	
	private Component setupBaseMaps() {
		
		baseLayers = getBaseLayers();
		currentBaseMap = baseLayers.get(0).getLayer();

		OptionGroup base = new OptionGroup("Base maps", baseLayers);
		base.setValue(baseLayers.get(0));
		base.setImmediate(true);
		
		
		base.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				currentBaseMap.setActive(false);
				LayerWrapper lw = (LayerWrapper) event.getProperty().getValue();
				lw.getLayer().setActive(true);
				currentBaseMap = lw.getLayer();
				trayNotify("Switched base map to " + lw.getDescription());
			}
		});
		
		for (LayerWrapper lw : baseLayers) {
			//map.addBaseLayer(lw.getLayer(), lw.getDescription());
			map.addLayer(lw.getLayer());
		}
		
		return base;
	}
	
	private List<LayerWrapper> getBaseLayers() {
		
		LTileLayer aer = new LTileLayer();
		aer.setUrl("http://basemap.nationalmap.gov/ArcGIS/rest/services/USGSImageryOnly/MapServer/tile/{z}/{y}/{x}");
		aer.setAttributionString(attrUSGS);
		aer.setActive(false);
		
		LTileLayer tf = new LTileLayer();
		tf.setUrl("http://{s}.tile.thunderforest.com/transport/{z}/{x}/{y}.png");
		tf.setAttributionString("Tiles Courtesy of <a href=\"http://www.thunderforest.com/\" target=\"_blank\">Thunderforest</a>" + 
								"&nbspand OpenStreetMap contributors");
		tf.setSubDomains(new String[]{"a", "b", "c"});
		tf.setActive(true);
		
		return Arrays.asList(new LayerWrapper("ThunderForest Transport ", tf), new LayerWrapper("USGS Aerial", aer));
	}
	
	private void trayNotify(String msg) {
		Notification.show(msg, Type.TRAY_NOTIFICATION);
	}
	private static class LayerWrapper {
		private String description;
		private LTileLayer layer;
		
		LayerWrapper(String description, LTileLayer layer) {
			super();
			this.description = description;
			this.layer = layer;
		}
		
		String getDescription() {
			return description;
		}
		
		LTileLayer getLayer() {
			return layer;
		}

		@Override
		public String toString() {
			return getDescription();
		}
	}
}
