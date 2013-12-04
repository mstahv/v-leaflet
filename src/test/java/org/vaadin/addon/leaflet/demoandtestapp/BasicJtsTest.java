package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;

import org.vaadin.addon.leaflet.AbstractLeafletLayer;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class BasicJtsTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A test for the JTS api";
	}

	private LMap leafletMap;
    private LFeatureGroup lfg;
    private  WKTReader wkt = new WKTReader();

    private MultiPolygon getMultiPolygon() {
        try {
            return (MultiPolygon) wkt.read("MULTIPOLYGON (((20 60, 21 60, 21 61, 20 60)), ((21 60, 22 60, 22 61, 21 60)))");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private  LineString getLineString() {
        try {
            return (LineString) wkt.read("LINESTRING (20 62, 21 62, 22 63)");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
    
	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();

		leafletMap.setControls(new ArrayList<Control>(Arrays.asList(Control
				.values())));

        lfg = new LFeatureGroup(); // Not creating a name -> not added to the
                                   // overlay controller

        MultiPolygon multiPolygon = getMultiPolygon();

        lfg.addMultiPolygon(multiPolygon);


		leafletMap.setZoomLevel(5);
        leafletMap.setCenter(multiPolygon.getCentroid());

        leafletMap.addComponent(lfg);


		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

		Button button = new Button("Delete first component from map");
		button.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Component next = leafletMap.iterator().next();
				leafletMap.removeComponent(next);
			}
		});
		content.addComponentAsFirst(button);

		button = new Button("Add polyline to map");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
			    
			    LineString ls = getLineString();
			    
			    lfg.addLineString(ls);

			}
		});
		content.addComponentAsFirst(button);

	}
}
