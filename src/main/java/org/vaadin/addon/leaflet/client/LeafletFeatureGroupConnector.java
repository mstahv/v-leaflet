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

import org.peimari.gleaflet.client.FeatureGroup;
import org.peimari.gleaflet.client.Layer;
import org.peimari.gleaflet.client.LayerGroup;

import com.vaadin.client.ServerConnector;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(org.vaadin.addon.leaflet.LFeatureGroup.class)
public class LeafletFeatureGroupConnector extends LeafletLayerGroupConnector {
	@Override
	protected LayerGroup createLayerGroup() {
		return FeatureGroup.create();
	}

	public AbstractLeafletLayerConnector<?> getConnectorFor(Layer iLayer) {
		for (ServerConnector c : getChildren()) {
			if (c instanceof AbstractLeafletLayerConnector<?>) {
				AbstractLeafletLayerConnector<?> lc = (AbstractLeafletLayerConnector<?>) c;
				if(lc.getLayer() == iLayer) {
					return lc;
				}
			}
		}
		return null;
	}

}
