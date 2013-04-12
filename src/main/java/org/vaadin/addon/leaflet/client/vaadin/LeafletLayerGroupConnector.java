/*
 * Copyright 2012 Vaadin Community.
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
package org.vaadin.addon.leaflet.client.vaadin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.others.LayerGroup;
import org.vaadin.addon.leaflet.LLayerGroup;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(LLayerGroup.class)
public class LeafletLayerGroupConnector extends
        AbstractLeafletLayerConnector<Object> implements
        HasComponentsConnector, ConnectorHierarchyChangeHandler {

    private ArrayList<ServerConnector> childrenToUpdate;

    private LayerGroup layerGroup;

    private boolean addedToParent = false;

    List<ComponentConnector> childComponents;

    public LeafletLayerGroupConnector() {
        super();
        addConnectorHierarchyChangeHandler(this);

    }

    @Override
    protected void init() {

    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // skipped
    }

    @Override
    public Label getWidget() {
        // no widget needed
        return super.getWidget();
    }

    @Override
    public LeafletLayerGroupState getState() {
        return (LeafletLayerGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        // if (!initialized) {
        childrenToUpdate = new ArrayList<ServerConnector>(getChildren());
        super.onStateChanged(stateChangeEvent);
        // }
        // update();

    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
        List<ComponentConnector> oldChildren = connectorHierarchyChangeEvent
                .getOldChildren();
        childrenToUpdate = new ArrayList<ServerConnector>();
        for (ServerConnector componentConnector : getChildren()) {
            if (!oldChildren.contains(componentConnector)) {
                childrenToUpdate.add(componentConnector);
            }
            oldChildren.remove(componentConnector);
        }
        /*
         * for (ComponentConnector componentConnector : oldChildren) {
         * AbstractLeafletLayerConnector<?> c =
         * (AbstractLeafletLayerConnector<?>) componentConnector;
         * map.removeLayer(c.getLayer()); }
         */

        // updateChildren();
    }

    @Override
    public void update() {

        if (!addedToParent) {
            ILayer layers[] = {};
            layerGroup = new LayerGroup(layers);
            addToParent(layerGroup);
            addedToParent = true;
        }
        updateChildren();

    }

    private void updateChildren() {

        if (childrenToUpdate != null) {
            for (ServerConnector serverConnector : childrenToUpdate) {

                AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) serverConnector;
                c.update();
                c.markUpdated();

            }
            childrenToUpdate = null;
        }
    }

    @Override
    public List<ComponentConnector> getChildComponents() {
        if (childComponents == null) {
            return Collections.emptyList();
        }

        return childComponents;
    }

    @Override
    public void setChildComponents(List<ComponentConnector> childComponents) {
        this.childComponents = childComponents;
    }

    @Override
    public HandlerRegistration addConnectorHierarchyChangeHandler(
            ConnectorHierarchyChangeHandler handler) {
        return ensureHandlerManager().addHandler(
                ConnectorHierarchyChangeEvent.TYPE, handler);
    }

    @Override
    protected Object createOptions() {
        return null;
    }

    @Override
    public ILayer getLayer() {
        return layerGroup;
    }

}
