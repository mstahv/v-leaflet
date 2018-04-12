package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import org.vaadin.addon.leaflet.LMap;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
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
        final Grid grid = new Grid();
        grid.setSizeFull();

        // Define some columns
        grid.addColumn("name", String.class);
        grid.addColumn("born", String.class);

        // Add some data rows
        grid.addRow("Nicolaus Copernicus", "");
        grid.addRow("Galileo Galilei", "");
        grid.addRow("Johannes Kepler", "");

        grid.setDetailsGenerator(new DetailsGenerator() {
            public Component getDetails(RowReference rowReference) {
                final LMap leafletMap = new LMap();
                final LTileLayer baselayer = new LTileLayer();
                baselayer.setAttributionString("OpenStreetMap");
                baselayer.setUrl("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
                leafletMap.addLayer(baselayer);
                leafletMap.setWidth("100%");
                leafletMap.setHeight("100px");
                leafletMap.setZoomLevel(3);
                LMarker leafletMarker = new LMarker(-21.54, 30.76);
                leafletMap.addComponent(leafletMarker);
                leafletMap.zoomToContent();
                return leafletMap;
            }
        });

        grid.addItemClickListener(new ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                grid.setDetailsVisible(itemId, !grid.isDetailsVisible(itemId));
            }
        });
        vl.addComponent(grid);

        return vl;
    }

}
