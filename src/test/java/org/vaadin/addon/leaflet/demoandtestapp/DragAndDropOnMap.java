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

import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.event.DropEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.addon.leaflet.shared.Point;

/**
 *
 * @author mstahv
 */
public class DragAndDropOnMap extends AbstractTest {
    
    @Override
    public String getDescription() {
        return "Test fractional zoom introduced in 1.0-beta1.";
    }
    
    private LMap leafletMap;
    
    @Override
    public Component getTestComponent() {
        
        leafletMap = new LMap();
        leafletMap.setWidth("300px");
        leafletMap.setHeight("300px");
        leafletMap.setCenter(0, 0);
        leafletMap.setZoomLevel(2.5);
        leafletMap.addLayer(new LOpenStreetMapLayer());
        
        Button button = new Button(FontAwesome.ANCHOR);
        button.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        
        DragSourceExtension dragFrom = new DragSourceExtension(button);
        dragFrom.setDragData("foo");
        
        DropTargetExtension<LMap> dt = new DropTargetExtension(leafletMap);
        dt.addDropListener((DropEvent<LMap> event) -> {
            int relativeX = event.getMouseEventDetails().getRelativeX();
            int relativeY = event.getMouseEventDetails().getRelativeY();
            leafletMap.translatePixelCoordinates(relativeX, relativeY, (Point p) -> {
                final LMarker lMarker = new LMarker(p);
                lMarker.setIcon(FontAwesome.ANCHOR);
                leafletMap.addLayer(lMarker);
            });
            Object dragData = event.getDragSourceExtension().get().getDragData();
            System.err.println(dragData);
        });
        
        return new VerticalLayout(button, leafletMap);
    }
}