package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class ZoomToExtendOnInitilaRender extends AbstractTest {

    @Override
    public String getDescription() {
        return "A simple test for zoom to extend on initial render";
    }


	@Override
	public Component getTestComponent() {
        final double[] latitude = {44.03664d, 43.961669d, 43.859999d};
        final double[] longitude = {11.161893d, 11.129769d, 11.061234d};

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        // Getting my map.
        LMap map = new LMap();
        map.setControls(new ArrayList<Control>(Arrays.asList(Control.values())));
        map.setSizeFull();

        // Setting backgroud layer.
		map.addBaseLayer(new LOpenStreetMapLayer(), "OSM");

        // I am here.
        LMarker myPositon = new LMarker(43.894367,11.078185);
        myPositon.setVisible(true);
        map.addComponent(myPositon);

        // Get random other point.
        int idxOther = Math.min((int)(Math.random() * 3.d), 2);

        // Put other point on map.
        LMarker leafletMarker = new LMarker(latitude[idxOther], longitude[idxOther]);
        map.addComponent(leafletMarker);

        // Build map bounds.
        Point[] mapPoints = {new Point(43.894367,11.078185), new Point(latitude[idxOther], longitude[idxOther])};       
        Bounds mapBounds = new Bounds(mapPoints);           

        // I'm expecting to see my map with two points.
        map.setCenter(mapBounds);
        map.zoomToExtent(mapBounds);

        layout.addComponent(map);
        return layout;
	}
}
