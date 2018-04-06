package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.DetailsGenerator;
import com.vaadin.ui.components.grid.ItemClickListener;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class MapInGridDetailsRow extends AbstractTest {

    @Override
    public String getDescription() {
        return "Map should work in Grid details rows.";
    }

    @Override
    public Component getTestComponent() {
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        final Grid<String> grid = new Grid<>();
        grid.setSizeFull();

        // Define some columns
        grid.addColumn(r -> r).setCaption("Name");
        grid.addColumn(r -> "").setCaption("Born");

        // Add some data rows
        grid.setItems("Nicolaus Copernicus","Galileo Galilei", "Johannes Kepler");

        grid.setDetailsGenerator((DetailsGenerator<String>) s -> {
            final LMap leafletMap = new LMap();
            final LTileLayer baselayer = new LTileLayer();
            baselayer.setAttributionString("OpenStreetMap");
            baselayer.setUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
            leafletMap.addLayer(baselayer);
            leafletMap.setWidth("100%");
            leafletMap.setHeight("100px");
            leafletMap.setZoomLevel(3);
            LMarker leafletMarker = new LMarker(-21.54, 30.76);
            leafletMap.addComponent(leafletMarker);
            leafletMap.zoomToContent();
            return leafletMap;
        });

        grid.addItemClickListener((ItemClickListener<String>) event -> grid.setDetailsVisible(event.getItem(), !grid.isDetailsVisible(event.getItem())));
        vl.addComponent(grid);

        return vl;
    }

}
