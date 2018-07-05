/*
 * Copyright 2018 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.dnd.event.DropEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.jsonmodels.ZoomPanOptions;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addonhelpers.AbstractTest;

/**
 *
 * @author mstahv
 */
public class DisabledZoomAnimation extends AbstractTest {

    @Override
    public String getDescription() {
        return "Test fractional zoom introduced in 1.0-beta1.";
    }

    private LMap leafletMap;
    private LMap leafletMap2;

    @Override
    public Component getTestComponent() {

        leafletMap = new LMap();
        // This affects the default animation, like via control
        leafletMap.setCustomInitOption("zoomAnimation", false);
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(61,23);
        leafletMap.setZoomLevel(2.5);
        leafletMap.addLayer(new LOpenStreetMapLayer());
        leafletMap.setCaption("without animation");

        leafletMap2 = new LMap();
        leafletMap2.setWidth("300px");
        leafletMap2.setHeight("300px");
        leafletMap2.setCenter(61,23);
        leafletMap2.setZoomLevel(2.5);
        leafletMap2.addLayer(new LOpenStreetMapLayer());
        leafletMap2.setCaption("with animation");
        
        ZoomPanOptions zoomPanOptions = new ZoomPanOptions();
        zoomPanOptions.setAnimate(false);

        ZoomPanOptions slow = new ZoomPanOptions();
        slow.setDuration(2.0);

        Button zoom = new Button("zoom");
        zoom.addClickListener(e -> {
            leafletMap.setZoomLevel(6, zoomPanOptions);
            leafletMap2.setZoomLevel(6, slow);
        });
        return new VerticalLayout(zoom, leafletMap, leafletMap2);
    }
}
