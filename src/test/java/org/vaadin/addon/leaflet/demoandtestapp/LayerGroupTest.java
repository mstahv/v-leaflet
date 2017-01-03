package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.ArrayList;
import java.util.Arrays;

import com.vaadin.data.HasValue;
import org.vaadin.addon.leaflet.AbstractLeafletLayer;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;

public class LayerGroupTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "A simple test";
    }

    LeafletClickListener listener = new LeafletClickListener() {

        @Override
        public void onClick(LeafletClickEvent event) {
            if (event.getPoint() != null) {
                Notification.show(String.format("Clicked %s @ %.4f,%.4f", event
                        .getConnector().getClass().getSimpleName(), event
                        .getPoint().getLat(), event.getPoint().getLon()));

                if (event.getSource() == leafletMap && addMarkers.getValue()) {
                    LMarker leafletMarker = new LMarker(event.getPoint());
                    leafletMarker.addClickListener(listener);
                    leafletMap.addComponent(leafletMarker);
                }
            } else {
                Notification.show(String.format("Clicked %s", event
                        .getConnector().getClass().getSimpleName()));
            }
            if (delete.getValue() && event.getSource() instanceof AbstractLeafletLayer) {
                Component component = (Component) event.getConnector();
                if (leafletMap.hasComponent(component)) {
                    leafletMap
                            .removeComponent((Component) event.getConnector());
                } else {
                    Notification
                            .show("Object inside a layer, will not be deleted");
                }

            }
        }
    };
    private LMap leafletMap;
    private CheckBox addMarkers;
    private CheckBox delete;
    private CheckBox showLayerGroupCB;
    private LLayerGroup llg;
    private LLayerGroup llgNested;
    private LLayerGroup llg2;

    @Override
    public Component getTestComponent() {
        leafletMap = new LMap();
        leafletMap.setCenter(60.4525, 22.301);
        leafletMap.setZoomLevel(15);

        leafletMap.setControls(new ArrayList<Control>(Arrays.asList(Control
                .values())));

        LPolyline leafletPolyline = null;

        // Adding to layergroup

        llg = new LLayerGroup(); // Not creating a name -> not added to the
                                 // overlay controller
        leafletPolyline = new LPolyline(new Point(60.45, 22.295), new Point(
                60.4555, 22.301), new Point(60.45, 22.307));
        leafletPolyline.setColor("#FF0000");
        leafletPolyline.setFill(true);
        leafletPolyline.setFillColor("#FFFFFF");
        leafletPolyline.addClickListener(listener);
        llg.addComponent(leafletPolyline);

        leafletPolyline = new LPolyline(
                new Point(60.45 + 0.005, 22.295 + 0.005), new Point(
                        60.4555 + 0.005, 22.301 + 0.005), new Point(
                        60.45 + 0.005, 22.307 + 0.005));
        leafletPolyline.setColor("#FFFFFF");
        leafletPolyline.setFill(true);
        leafletPolyline.setFillColor("#FF0000");
        leafletPolyline.addClickListener(listener);
        llg.addComponent(leafletPolyline);

        LCircle leafletCircle = new LCircle(60.4525 + 0.005, 22.301 + 0.005,
                200);
        leafletCircle.setColor("#FF0000");
        llgNested = new LLayerGroup();
        llgNested.addComponent(leafletCircle);
        llg.addComponent(llgNested);

        llg2 = new LLayerGroup();
        leafletCircle = new LCircle(60.4525 - 0.005, 22.301 - 0.005, 20);
        leafletCircle.setColor("#00FF00");
        llg2.addComponent(leafletCircle);
        leafletCircle = new LCircle(60.4525 - 0.008, 22.301 - 0.008, 20);
        leafletCircle.setColor("#00FF00");
        llg2.addComponent(leafletCircle);
        leafletCircle = new LCircle(60.4525 - 0.011, 22.301 - 0.011, 20);
        leafletCircle.setColor("#00FF00");
        llg2.addComponent(leafletCircle);
        leafletCircle = new LCircle(60.4525 - 0.014, 22.301 - 0.014, 20);
        leafletCircle.setColor("#00FF00");
        llg2.addComponent(leafletCircle);

        leafletMap.addOverlay(llg, null);
        leafletMap.addOverlay(llg2, "Small circles group");

        leafletCircle = new LCircle(60.4525, 22.301, 300);
        leafletCircle.setColor("#00FFFF");
        // leafletCircle.addClickListener(listener);
        leafletMap.addComponent(leafletCircle);

        LMarker leafletMarker = new LMarker(60.4525, 22.301);
        leafletMarker.addClickListener(listener);
        leafletMap.addComponent(leafletMarker);

        leafletMarker = new LMarker(60.4525, 22.301);
        leafletMarker.setIcon(new ClassResource("testicon.png"));
        leafletMarker.setIconSize(new Point(57, 52));
        leafletMarker.setIconAnchor(new Point(57, 26));
        leafletMarker.addClickListener(listener);
        leafletMap.addComponent(leafletMarker);

        leafletMap.addBaseLayer(new LOpenStreetMapLayer(), "OSM");

        leafletMap.addClickListener(listener);

        leafletMap.addMoveEndListener(new LeafletMoveEndListener() {
            @Override
            public void onMoveEnd(LeafletMoveEndEvent event) {
                Bounds b = event.getBounds();
                Notification.show(
                        String.format("New viewport (%.4f,%.4f ; %.4f,%.4f)",
                                b.getSouthWestLat(), b.getSouthWestLon(),
                                b.getNorthEastLat(), b.getNorthEastLon()),
                        Type.TRAY_NOTIFICATION);
            }
        });

        return leafletMap;
    }

    @Override
    protected void setup() {
        super.setup();

        addMarkers = new CheckBox("Add markers");
        content.addComponentAsFirst(addMarkers);

        delete = new CheckBox("Delete on click");
        content.addComponentAsFirst(delete);

        showLayerGroupCB = new CheckBox(
                "Show first layer (switch on/off from server side)");
        showLayerGroupCB.setValue(true);
        content.addComponentAsFirst(showLayerGroupCB);

        showLayerGroupCB.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {

            @Override
            public void valueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    if (!leafletMap.hasComponent(llg)) {
                        leafletMap.addComponent(llg);
                    }
                } else {
                    leafletMap.removeComponent(llg);
                }

            }
        });

        Button button = new Button(
                "Delete first component from map (may also be a layer containing many components)");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	for (Component c : leafletMap) {
            		if(!(c instanceof LTileLayer)) {
            			leafletMap.removeComponent(c);
            			break;
            		}
				}
            }
        });
        content.addComponentAsFirst(button);
        button = new Button(
                "Delete first component from first layer group (may also be a layer containing many components)");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                LLayerGroup group = null;
                for (Component c : leafletMap) {
                    if (c instanceof LLayerGroup) {
                        group = (LLayerGroup) c;
                        break;
                    }
                }
                if(group.getComponentCount() > 0) {
                	Component next = group.iterator().next();
                	group.removeComponent(next);
                } else {
                	Notification.show("No component in first component group");
                }
            }
        });
        content.addComponentAsFirst(button);

        button = new Button(
                "Add polyline to first group on map (creates if does not exist)");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                LLayerGroup group = null;
                for (Component c : leafletMap) {
                    if (c instanceof LLayerGroup) {
                        group = (LLayerGroup) c;
                        break;
                    }
                }
                if (group == null) {
                    group = new LLayerGroup();
                    leafletMap.addOverlay(group, "new group");
                }

                LPolyline lPolyline = new LPolyline(new Point(60.44, 22.30),
                        new Point(60.456, 22.304));
                lPolyline.addClickListener(listener);
                group.addComponent(lPolyline);
            }
        });
        content.addComponentAsFirst(button);

    }
}
