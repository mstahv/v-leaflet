package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addonhelpers.AbstractTest;

@SuppressWarnings("serial")
public class InitialViewPortDetectionWithFixedSize extends AbstractTest {

    private final LMap leafletMap = new LMap();

    private final Label zoomLabel = new Label();

    @Override
    public String getDescription() {
        return "Test that inial size is reported to server if move end listeners"
                + " is set";
    }

    @Override
    public Component getTestComponent() {
        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        leafletMap.addBaseLayer(layer, "OSM");
        leafletMap.setCenter(42.3625, -71.112);
        leafletMap.setZoomLevel(15);

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
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
    }

    private void updateZoomLabel() {
        zoomLabel.setCaption(
                             "Current zoom: " + leafletMap.getZoomLevel()
                + " Viewport: " + leafletMap.getBounds()
        
        
        );
    }

}
