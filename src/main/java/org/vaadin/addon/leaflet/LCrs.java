/*
 * Copyright 2014 Vaadin Community.
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
package org.vaadin.addon.leaflet;

import org.peimari.gleaflet.client.Crs;

/**
 * Encapsulates a Coordinate Reference System for v-leaflet. Written so that
 * alternative identification systems can be handled in the future, eg Spatial
 * Reference.<p>Constructor left accessible so that user can create their own
 * CRSs if, and when, support in g-leaflet is extended (see {@link Crs})</p>
 *
 *
 * @author Warwick Dufour
 */
public enum LCrs {

	/**
	 * Leaflet specific
	 */
	SIMPLE("Simple"),
	/**
	 * WGS 84
	 */
	EPSG4326("EPSG:4326"),
	/**
	 * WGS 84 / World Mercator
	 */
	EPSG3395("EPSG:3395"),
	/**
	 * WGS 84 / Pseudo-Mercator
	 */
	EPSG3857("EPSG:3857");
	private String id;

	/**
	 * Constructor left accessible so that user can create their own
	 * CRSs if and when support in g-leaflet is extended (see {@link Crs})
	 *
	 * @param s
	 */
	LCrs(String s) {
		id = s;
	}

	String getId() {
		return id;
	}
}
