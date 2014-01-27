package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.demoandtestapp.util.AbstractTest;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDrawRectangle;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vividsolutions.jts.geom.Geometry;

public class SelectRectangularArea extends AbstractTest {

	@Override
	public String getDescription() {
		return "Test leaflet draw";
	}

	private LMap leafletMap;
	private LLayerGroup group;

	@Override
	public Component getTestComponent() {

		leafletMap = new LMap();
		leafletMap.setCenter(0, 0);
		leafletMap.setZoomLevel(0);
		leafletMap.addLayer(new LTileLayer(
				"http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));

		return leafletMap;
	}

    private void goToAreaSelectionMode() {
        LDrawRectangle draw = new LDrawRectangle();
        draw.addFeatureDrawnListener(new FeatureDrawnListener() {
            
            @Override
            public void featureDrawn(FeatureDrawnEvent event) {
                final Geometry jtsGeometryDefiningTheSelectedArea = event.getDrawnFeature().getGeometry();
                Notification.show("Selected area: " + jtsGeometryDefiningTheSelectedArea);
            }
        });
        draw.addTo(leafletMap);
    }

    @Override
    protected void setup() {
        super.setup();
        Button button = new Button("Select area (S)", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                goToAreaSelectionMode();
            }
        });
        button.setClickShortcut(ShortcutAction.KeyCode.S, null);
        content.addComponentAsFirst(button);
    }
    
    

}
