package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.addonhelpers.AbstractTest;

import java.util.Collection;

public class PolygonWithHolesTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "A test for the JTS api";
    }

    private LMap leafletMap;
    private LFeatureGroup lfg;

    private WKTReader wkt = new WKTReader();

    private Geometry readWKT(String wktString) {
        try {
            return wkt.read(wktString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Polygon getPolygon() {
        return (Polygon) readWKT("POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10)," +
                "(20 30, 35 35, 30 20, 20 30))");
    }

    public Component getTestComponent() {
        leafletMap = new LMap();

        lfg = new LFeatureGroup(); // Not creating a name -> not added to the
                                   // overlay controller


        LPolygon polygon = new LPolygon();
        polygon.setPoints(new Point[]{new Point(0,0),new Point(30,30),new Point(30,0)});
        polygon.setHoles(new Point[]{new Point(20, 20), new Point(25, 25), new Point(25, 20)});
        // non complete hole
        polygon.setHoles(new Point[]{new Point(5, 10), new Point(15, 15), new Point(15, 10)});
        polygon.setColor("green");
        lfg.addComponent(polygon);
        Polygon poly = getPolygon();
        Collection<LeafletLayer> lPoly = JTSUtil.toLayers(poly);
        lfg.addComponent(lPoly);

        leafletMap.setZoomLevel(5);

        leafletMap.addComponent(lfg);

        leafletMap.zoomToContent();

        return leafletMap;
    }
}
