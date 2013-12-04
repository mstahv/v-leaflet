package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.JTSUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
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

    private Polygon getPolygon() {
        try {
            return (Polygon) wkt.read("POLYGON ((20 64, 21 64, 21 65, 20 64))");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MultiPolygon getMultiPolygon() {
        try {
            return (MultiPolygon) wkt.read("MULTIPOLYGON (((20 60, 21 60, 21 61, 20 60)), ((21 60, 22 60, 22 61, 21 60)))");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  LineString getLineString() {
        try {
            return (LineString) wkt.read("LINESTRING (20 62, 21 62, 22 63)");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  MultiLineString getMultiLineString() {
        try {
            return (MultiLineString) wkt.read("MULTILINESTRING ((20 62, 21 62, 22 63), (20 64, 21 65, 22 66))");
        } catch (ParseException e) {
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

        Collection<LeafletLayer> lMultiPoly = JTSUtil.toLayers(multiPolygon);

        for (LeafletLayer leafletLayer : lMultiPoly) {
            lfg.addComponents(leafletLayer);
        }


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
			    
			    MultiLineString multiLineString = getMultiLineString();
			    
			    Collection<LeafletLayer> layers = JTSUtil.toLayers(multiLineString);
			    
			    for (LeafletLayer leafletLayer : layers) {
			        lfg.addComponent(leafletLayer);
                }

			}
		});
		content.addComponentAsFirst(button);

	}
}
