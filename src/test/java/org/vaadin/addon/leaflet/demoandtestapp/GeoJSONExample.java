package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.opengis.feature.Feature;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.addonhelpers.AbstractTest;

public class GeoJSONExample extends AbstractTest {

    @Override
    public String getDescription() {
        return "Example how to use GeoJSON data.";
    }

    private LMap leafletMap;

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();
        leafletMap.setWidth("600px");
        leafletMap.setHeight("400px");

        /*
         * Note, this is just one option to read GeoJSON in java. Here, using 
         * helper from geotools library. In some simple cases approach to use
         * plain Json library like Jackson or GSON might be better.
         */
        FeatureJSON io = new FeatureJSON();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            // Look ma, no proxy needed, how cool is that!

            FeatureCollection fc = io.readFeatureCollection(new URL("http://eric.clst.org/assets/wiki/uploads/Stuff/gz_2010_us_040_00_500k.json").openStream());
            Logger.getLogger(GeoJSONExample.class.getName()).severe("Download in " + (System.currentTimeMillis() - currentTimeMillis));
            currentTimeMillis = System.currentTimeMillis();

            FeatureIterator iterator = fc.features();
            try {
                while (iterator.hasNext()) {
                    Feature feature = iterator.next();
                    final String name = feature.getProperty("NAME").getValue().toString();
                    System.out.println("State " + name + " read!");
                    Geometry geometry = (Geometry) feature.getDefaultGeometryProperty().getValue();

                    // The geojson provided in example is rather complex (several megabytes)
                    // Use JTS to simplyfy. Note that it is rather easy to use 
                    // different settings on different zoom levels, as well as decide
                    // to drop the feature form client altogether
                    geometry = DouglasPeuckerSimplifier.simplify(geometry, 0.2);

                    // In this example can be Polygon/Multipolygon
                    Collection<LeafletLayer> toLayers = JTSUtil.toLayers(geometry);
                    for (LeafletLayer l : toLayers) {
                        leafletMap.addComponent(l);
                        if (l instanceof LPolygon) {
                            LPolygon lPolygon = (LPolygon) l;
                            lPolygon.addClickListener(new LeafletClickListener() {

                                @Override
                                public void onClick(LeafletClickEvent event) {
                                    Notification.show("That is " + name);
                                }
                            });
                        }
                    }
                }
                Logger.getLogger(GeoJSONExample.class.getName()).severe("Reducing and creating layers " + (System.currentTimeMillis() - currentTimeMillis));

            } finally {
                iterator.close();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(GeoJSONExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeoJSONExample.class.getName()).log(Level.SEVERE, null, ex);
        }

        leafletMap.zoomToContent();

        return leafletMap;
    }
}
