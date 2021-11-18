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
package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractComponentContainerState;

/**
 *
 * @author mattitahvonenitmill
 */
public class LeafletMapState extends AbstractComponentContainerState {

	public Point center;
	public Double zoomLevel;

	public Bounds zoomToExtent;

	public String attributionPrefix = "Leaflet";
    public Bounds maxBounds;
    public Integer minZoom;
    public Integer maxZoom;

    /* This should be replaced with a more decent api */
    public String customMapOptionsJson;

	/**
	 * Internal String identifier of CRS that is meaningful to v-leaflet
	 * client.
	 */
	public String crsName;
	public String newCrsName;
	public String newCrsProjection;
	public double newCrsA;
	public double newCrsB;
	public double newCrsC;
	public double newCrsD;
	public double newCrsMinX;
	public double newCrsMinY;
	public double newCrsMaxX;
	public double newCrsMaxY;

	public Boolean dragging;
	public Boolean touchZoom;
	public Boolean doubleClickZoom;
	public Boolean boxZoom;
	public Boolean scrollWheelZoom;
	public Double zoomSnap;
	public Double zoomDelta;
	public Boolean keyboard;
	public Boolean readOnly;
	public Connector[] updateLayersOnLocate;

    public int minLocateInterval = 5000;
}
