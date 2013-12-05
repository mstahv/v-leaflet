package org.vaadin.addon.leaflet;

import java.util.Collection;


public class LFeatureGroup extends LLayerGroup {

    public void addComponent(Collection<LeafletLayer> leafletLayers) {
        for (LeafletLayer leafletLayer : leafletLayers) {
            addComponent(leafletLayer);
        }
    }

}
