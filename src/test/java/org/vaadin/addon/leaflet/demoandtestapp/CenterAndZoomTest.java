package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Arrays;
import java.util.List;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class CenterAndZoomTest extends AbstractTest {

	String attrUSGS = "Tiles courtesy of U.S. Geological Survey";

	private LMap map;
	private LMarker marker;
	
	private LTileLayer currentBaseMap;
	
	@Override
	public String getDescription() {
		return "Center and Zoom Asymmetry test";
	}

	@Override
	public Component getTestComponent() {
		
		map = new LMap();
		map.setCenter(40, -105.2);
		map.setZoomLevel(6);
		LTileLayer tf = new LTileLayer();
		tf.setUrl("http://{s}.tile.thunderforest.com/transport/{z}/{x}/{y}.png");
		tf.setAttributionString("Tiles Courtesy of <a href=\"http://www.thunderforest.com/\" target=\"_blank\">Thunderforest</a>" + 
								"&nbspand OpenStreetMap contributors");
		tf.setSubDomains(new String[]{"a", "b", "c"});
		tf.setActive(true);
		map.addBaseLayer(tf, "ThunderForest Transport");
		
		
		return map;
	}

	
	@Override
	protected void setup() {
		super.setup();
		
		Label l = new Label("Results differ depending on whether zoom or center is done first.<br/>" +
				"When zoom is done first, you have to zoom out a couple clicks to see the marker.",
				ContentMode.HTML);
		l.setWidth("400px");
		
		HorizontalLayout hl = new HorizontalLayout();
		
		Button b1 = new Button("Center then Zoom");
		b1.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doWork("c", "z");
			}
		});
		
		Button b2 = new Button("Zoom then Center");
		b2.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doWork("z", "c");
			}
		});
		
		Button b3 = new Button("setView");
		b3.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doWork("v");
			}
		});	
		
		hl.addComponents(l, b1, b2, b3);
		hl.setSpacing(true);
		
		content.addComponentAsFirst(hl);
	}

	private void doWork(String...ops) {
		trayNotify("got click");
		if (marker != null) {
			map.removeComponent(marker);
			marker = null;
		}
		double lat = 36;
		double lng = -108;
		int zoom = 12;
		marker = new LMarker(lat, lng);
		
		StringBuilder sb = new StringBuilder();

		map.addComponent(marker);
		sb.append(" marker");
		
		for (String op : ops) {
			if ("c".equals(op)) {
				map.setCenter(lat, lng);
				sb.append(" center");
			} else if ("z".equals(op)) {
				map.setZoomLevel(zoom);
				sb.append(" zoom");
			} else if ("v".equals(op)) {
				map.setView(lat, lng, zoom);
				//Notification.show("Not yet implemented");
				sb.append(" setView");
			}
		}

		trayNotify("Operation sequence:" + sb.toString());
	}
	
	private void trayNotify(String msg) {
		Notification.show(msg, Type.TRAY_NOTIFICATION);
	}
}
