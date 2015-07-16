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
package org.vaadin.addon.leaflet.client;

import org.vaadin.addon.leaflet.shared.LeafletLayerGroupState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.peimari.gleaflet.client.Layer;
import org.peimari.gleaflet.client.LayerGroup;
import org.vaadin.addon.leaflet.LLayerGroup;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(LLayerGroup.class)
public class LeafletLayerGroupConnector extends
		AbstractLeafletLayerConnector<Object> implements
		HasComponentsConnector, ConnectorHierarchyChangeHandler {

	private LayerGroup layerGroup;

	List<ComponentConnector> childComponents;

	private ArrayList<ComponentConnector> orphaned;

	public LeafletLayerGroupConnector() {
		super();
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// skipped
	}

	@Override
	public LeafletLayerGroupState getState() {
		return (LeafletLayerGroupState) super.getState();
	}
	
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		// NOP
	}
	@Override
	public void onConnectorHierarchyChange(
			ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
	}

	@Override
	public void update() {

		if (layerGroup == null) {
			layerGroup = createLayerGroup();
			addToParent(layerGroup);
		}
		updateChildren();

	}

	protected LayerGroup createLayerGroup() {
		return LayerGroup.create();
	}

	private void updateChildren() {
		for (ServerConnector serverConnector : getChildComponents()) {
			AbstractLeafletLayerConnector<?> c = (AbstractLeafletLayerConnector<?>) serverConnector;
			c.updateIfDirty();
		}
		if (orphaned != null) {
			for (ComponentConnector c : orphaned) {
				AbstractLeafletLayerConnector<?> lc = (AbstractLeafletLayerConnector<?>) c;
				lc.removeLayerFromParent();
			}
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
		// Firing hierarchy changes events seems to be buggy in V7 :-(
		// As a workaround do the things here
		orphaned = new ArrayList<ComponentConnector>(getChildComponents());
		this.childComponents = childComponents;
		for (ServerConnector componentConnector : childComponents) {
			orphaned.remove(componentConnector);
		}
		markDirty();
		deferUpdate();
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
	public Layer getLayer() {
		return layerGroup;
	}

}
