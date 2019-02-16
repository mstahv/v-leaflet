package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Collection;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;

public class BasicJtsTest extends AbstractTest {

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

    private Point getPoint() {
        return (Point) readWKT("POINT (23 64)");
    }

    private MultiPoint getMultiPoint() {
        return (MultiPoint) readWKT("MULTIPOINT ((24 64), (25 65))");
    }

    private Polygon getPolygon() {
        return (Polygon) readWKT("POLYGON ((20 64, 21 64, 21 65, 20 64))");
    }

    private MultiPolygon getMultiPolygon() {
        return (MultiPolygon) readWKT("MULTIPOLYGON (((20 60, 21 60, 21 61, 20 60)), ((21 60, 22 60, 22 61, 21 60)))");
    }

    private LineString getLineString() {
        return (LineString) readWKT("LINESTRING (20 62, 21 62, 22 63)");
    }

    private MultiLineString getMultiLineString() {
        return (MultiLineString) readWKT("MULTILINESTRING ((20 62, 21 62, 22 63), (20 64, 21 65, 22 66))");
    }

    public Component getTestComponent() {
        leafletMap = new LMap();

        lfg = new LFeatureGroup(); // Not creating a name -> not added to the
                                   // overlay controller

        Polygon poly = getPolygon();
        Collection<LeafletLayer> lPoly = JTSUtil.toLayers(poly);
        lfg.addComponent(lPoly);

        MultiPolygon multiPolygon = getMultiPolygon();
        Collection<LeafletLayer> lMultiPoly = JTSUtil.toLayers(multiPolygon);
        lfg.addComponent(lMultiPoly);

        LineString lineString = getLineString();
        Collection<LeafletLayer> lLine = JTSUtil.toLayers(lineString);
        lfg.addComponent(lLine);

//        MultiLineString multiLineString = getMultiLineString();
//        Collection<LeafletLayer> lMultiLine = JTSUtil.toLayers(multiLineString);
//        lfg.addComponent(lMultiLine);

        MultiPoint multiPoint = getMultiPoint();
        Collection<LeafletLayer> lMultiPoint = JTSUtil.toLayers(multiPoint);
        lfg.addComponent(lMultiPoint);

        Point point = getPoint();
        Collection<LeafletLayer> lPoint = JTSUtil.toLayers(point);
        lfg.addComponent(lPoint);

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

                Collection<LeafletLayer> layers = JTSUtil
                        .toLayers(multiLineString);

                for (LeafletLayer leafletLayer : layers) {
                    lfg.addComponent(leafletLayer);
                }

            }
        });
        content.addComponentAsFirst(button);

    }
}
