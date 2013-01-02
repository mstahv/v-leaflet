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

import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.AbstractComponentState;

/**
 *
 * @author mattitahvonenitmill
 */
public class LeafletMapState extends AbstractComponentState {
	
	public Point center;
	public Integer zoomLevel;
	
	private BaseLayer[] baseLayers;

	public BaseLayer[] getBaseLayers() {
		return baseLayers;
	}
	
	public void setBaseLayers(BaseLayer[] baseLayers) {
		this.baseLayers = baseLayers;
	}
	
	
	
}
