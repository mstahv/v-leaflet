package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.addonhelpers.AbstractTest;

public class ChoroplethExample extends AbstractTest {

    @Override
    public String getDescription() {
        return "Example how to build a 'Choropleth' map.";
    }

    private LMap leafletMap;

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();
        leafletMap.addLayer(new LOpenStreetMapLayer());
        leafletMap.setView(37.8, -96.0, 4.0);


        /*
         * Reading from geojson here, but typically you'd just query
         * your DB directly for the data.
         */
        FeatureJSON io = new FeatureJSON();
        try {
            // 
            // 
            FeatureCollection fc = io.readFeatureCollection(getClass().getResourceAsStream("/us-states.json"));

            FeatureIterator iterator = fc.features();
            try {
                //fixme geotools still uses pre-location tech jts
//                while (iterator.hasNext()) {
//                    Feature feature = iterator.next();
//
//                    final String name = feature.getProperty("name").getValue().toString();
//                    final Double density = (Double) feature.getProperty("density").getValue();
//                    System.out.println("State " + name + " read!");
//                    Geometry geometry = (Geometry) feature.getDefaultGeometryProperty().getValue();
//
//                    // Using a helper create v-leaflet components from geojson
//                    Collection<LeafletLayer> toLayers = JTSUtil.toLayers(geometry);
//                    for (LeafletLayer l : toLayers) {
//                        leafletMap.addComponent(l);
//                        if (l instanceof AbstractLeafletVector) {
//                            configureFeature(l, density, name);
//                        } else if (l instanceof LLayerGroup) {
//                            LLayerGroup g = (LLayerGroup) l;
//                            for (Component component : g) {
//                                configureFeature((LeafletLayer) component, density, name);
//                            }
//                        }
//                    }
//                }
            } finally {
                iterator.close();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ChoroplethExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChoroplethExample.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
         * AbsoluteLayout is a handy layout you can use to place any Vaadin
         * components on top of map. Here we just use raw html label to create
         * a legend, but we could use dynamically generated html or e.g. Table
         * component on top of the map as well.
         */
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.setWidth("800px");
        absoluteLayout.setHeight("500px");
        absoluteLayout.addComponent(leafletMap);

        Label label = new Label("<style>.legend { background:white; padding:10px; border-radius: 4px; text-align: left; line-height: 18px; color: #555; } .legend i { width: 18px; height: 18px; float: left; margin-right: 8px; opacity: 0.7; }</style><div class=\"info legend leaflet-control\"><i style=\"background:#FFEDA0\"></i> 0–10<br><i style=\"background:#FED976\"></i> 10–20<br><i style=\"background:#FEB24C\"></i> 20–50<br><i style=\"background:#FD8D3C\"></i> 50–100<br><i style=\"background:#FC4E2A\"></i> 100–200<br><i style=\"background:#E31A1C\"></i> 200–500<br><i style=\"background:#BD0026\"></i> 500–1000<br><i style=\"background:#800026\"></i> 1000+</div>", ContentMode.HTML);
        label.setWidth("100px");
        absoluteLayout.addComponent(label, "bottom: 30px; right: 20px;");

        return absoluteLayout;
    }

    protected void configureFeature(LeafletLayer l, final Double density, final String name) {
        AbstractLeafletVector lPolygon = (AbstractLeafletVector) l;
        lPolygon.setFillColor(getColor(density));
        lPolygon.setColor("white");
        lPolygon.setDashArray("3");
        lPolygon.setWeight(2);
        lPolygon.setFillOpacity(0.7);
        lPolygon.addClickListener(new LeafletClickListener() {

            @Override
            public void onClick(LeafletClickEvent event) {
                // Any server side data access is easy to do here, could for 
                // example do a DB query and show more detailed data for the 
                // selected feature
                Notification.show("In " + name + ", the population is " + density + "people/mile²");
            }
        });
        lPolygon.setLineCap("");

    }

    String getColor(double d) {
        return d > 1000 ? "#800026"
                : d > 500 ? "#BD0026"
                        : d > 200 ? "#E31A1C"
                                : d > 100 ? "#FC4E2A"
                                        : d > 50 ? "#FD8D3C"
                                                : d > 20 ? "#FEB24C"
                                                        : d > 10 ? "#FED976"
                                                                : "#FFEDA0";
    }

}
