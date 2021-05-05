package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.type.GeometryType;
import org.vaadin.addon.leaflet.*;
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

            FeatureCollection fc = io.readFeatureCollection(new URL("https://gist.githubusercontent.com/hrbrmstr/91ea5cc9474286c72838/raw/59421ff9b268ff0929b051ddafafbeb94a4c1910/continents.json").openStream());
            Logger.getLogger(GeoJSONExample.class.getName()).severe("Download in " + (System.currentTimeMillis() - currentTimeMillis));
            currentTimeMillis = System.currentTimeMillis();

            FeatureIterator iterator = fc.features();
            try {
                while (iterator.hasNext()) {
                    Feature feature = iterator.next();
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
                        if (l instanceof LLayerGroup) {
                            LLayerGroup group = (LLayerGroup) l;
                            Iterator<Component> components = group.getComponentIterator();
                            while(components.hasNext()) {
                                LPolygon lPolygon = (LPolygon) components.next();
                                lPolygon.setStroke(false);
                                lPolygon.setFillColor("brown");
                            }
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
