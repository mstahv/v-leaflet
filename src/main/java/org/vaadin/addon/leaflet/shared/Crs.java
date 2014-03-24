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
package org.vaadin.addon.leaflet.shared;

/**
 * Encapsulates a Coordinate Reference System for v-leaflet
 *
 * @author Warwick Dufour
 */
public class Crs {

	/**
	 * Leaflet specific
	 */
	public static final Crs Simple = new Crs("Simple");
	/**
	 * WGS 84
	 */
	public static final Crs EPSG4326 = new Crs("EPSG4326");
	/**
	 * WGS 84 / World Mercator
	 */
	public static final Crs EPSG3395 = new Crs("EPSG3395");
	/**
	 * WGS 84 / Pseudo-Mercator
	 */
	public static final Crs EPSG3857 = new Crs("EPSG3857");
	
	private final String name;

	public Crs(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
