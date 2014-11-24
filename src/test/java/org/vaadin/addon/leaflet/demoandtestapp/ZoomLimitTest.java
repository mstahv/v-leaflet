package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addonhelpers.AbstractTest;

@SuppressWarnings("serial")
public class ZoomLimitTest extends AbstractTest {

    private final int MIN_ZOOM_LEVEL = 10;
    private final int MAX_ZOOM_LEVEL = 16;

    private final LMap leafletMap = new LMap();

    private final Label zoomLabel = new Label();

    @Override
    public String getDescription() {
        return "Test for min/max zoom";
    }

    @Override
    public Component getTestComponent() {
        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(42.3625, -71.112);
        leafletMap.setZoomLevel(15);

        leafletMap.setMinZoom(MIN_ZOOM_LEVEL);
        leafletMap.setMaxZoom(MAX_ZOOM_LEVEL);

        leafletMap.addMoveEndListener(new LeafletMoveEndListener() {
            @Override
            public void onMoveEnd(LeafletMoveEndEvent event) {
                updateZoomLabel();
            }
        });

        return leafletMap;
    }

    @Override
    protected void setup() {
        super.setup();
        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(zoomLabel);
        content.addComponentAsFirst(layout);
        updateZoomLabel();
    }

    private void updateZoomLabel() {
        zoomLabel.setCaption("Min zoom: " + MIN_ZOOM_LEVEL + " - " +
                             "Max zoom: " + MAX_ZOOM_LEVEL + " - " +
                             "Current zoom: " + leafletMap.getZoomLevel());
    }

}
