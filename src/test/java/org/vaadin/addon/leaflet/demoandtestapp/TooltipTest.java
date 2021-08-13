package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shared.TooltipState;
import org.vaadin.addonhelpers.AbstractTest;

@StyleSheet({"testStyles.css"})
public class TooltipTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "Tooltip test case";
    }

    private LMap leafletMap;

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();

        leafletMap.addLayer(new LOpenStreetMapLayer());

        leafletMap.setCenter(60.4525, 22.301);
        leafletMap.setZoomLevel(15);

        LTooltip tooltip1 = new LTooltip(60.4540, 22.275).setContent("Hi, I'm a standalone tooltip in magenta!");
        tooltip1.setPrimaryStyleName("tooltip-custom-style-test");
        leafletMap.addComponent(tooltip1);

        LMarker marker1 = new LMarker(60.4720, 22.271);
        marker1.setTooltip("Permanent tooltip bound to marker");
        TooltipState marker1TooltipState = new TooltipState();
        marker1TooltipState.permanent = true;
        marker1.setTooltipState(marker1TooltipState);
        leafletMap.addComponent(marker1);

        LMarker marker2 = new LMarker(60.4620, 22.281);
        marker2.setTooltip("Auto direction tooltip bound to marker with transparency");
        TooltipState marker2TooltipState = new TooltipState();
        marker2TooltipState.opacity = 0.5;
        marker2.setTooltipState(marker2TooltipState);
        leafletMap.addComponent(marker2);

        LMarker marker3 = new LMarker(60.4620, 22.276);
        marker3.setTooltip("Top direction tooltip bound to marker");
        TooltipState marker3TooltipState = new TooltipState();
        marker3TooltipState.direction = "top";
        marker3.setTooltipState(marker3TooltipState);
        leafletMap.addComponent(marker3);

        LPolyline polyline1 = new LPolyline(new Point(60.4527, 22.291), new Point(60.4549, 22.309));
        polyline1.setTooltip("Tooltip bound to polyline");
        leafletMap.addComponent(polyline1);

        LPolygon polygon1 = new LPolygon(new Point(60.4525, 22.301), new Point(60.4539, 22.309),
                new Point(60.4535, 22.321), new Point(60.4525, 22.301));
        polygon1.setTooltip("Sticky tooltip bound to polygon");
        TooltipState polygon1TooltipState = new TooltipState();
        polygon1TooltipState.sticky = true;
        polygon1.setTooltipState(polygon1TooltipState);
        leafletMap.addComponent(polygon1);

        leafletMap.zoomToContent();

        return leafletMap;

    }

}
