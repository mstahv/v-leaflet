package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.vaadin.LeafletLayerGroupState;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

// If LLayerGroup should be able to be a leafletlayer, one would need to implement HasComponents
public class LLayerGroup extends AbstractComponentContainer {

    private List<Component> components = new ArrayList<Component>();
    private final String name;

    public LLayerGroup(String name) {
        this.name = name;

    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {

    }

    @Override
    public void addComponent(Component c) {
        if ((!(c instanceof LeafletLayer)) && (!(c instanceof LLayerGroup))) {
            throw new IllegalArgumentException(
                    "only instances of LeafletLayer or LLayerGroup allowed");
        }
        super.addComponent(c);
        components.add(c);
        markAsDirty();
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        components.remove(c);
        markAsDirty();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().name = name;
    }

    @Override
    protected LeafletLayerGroupState getState() {
        return (LeafletLayerGroupState) super.getState();
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

}
