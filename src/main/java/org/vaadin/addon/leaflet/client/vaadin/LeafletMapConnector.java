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

import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.controls.ControlOptions;
import org.discotools.gwt.leaflet.client.controls.Position;
import org.discotools.gwt.leaflet.client.controls.layers.Layers;
import org.discotools.gwt.leaflet.client.controls.scale.Scale;
import org.discotools.gwt.leaflet.client.controls.scale.ScaleOptions;
import org.discotools.gwt.leaflet.client.controls.zoom.Zoom;
import org.discotools.gwt.leaflet.client.controls.zoom.ZoomOptions;
import org.discotools.gwt.leaflet.client.crs.epsg.EPSG3857;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;
import org.discotools.gwt.leaflet.client.map.Map;
import org.discotools.gwt.leaflet.client.map.MapOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.widget.MapWidget;
import org.vaadin.addon.leaflet.LeafletMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author mattitahvonenitmill
 */
@Connect(LeafletMap.class)
public class LeafletMapConnector extends AbstractHasComponentsConnector {
	
	static {
		LeafletResourceInjector.ensureInjected();
	}

	LeafletMapServerRpc rpc = RpcProxy.create(LeafletMapServerRpc.class, this);
	private Map map;
	private EPSG3857 vCRS_EPSG3857 = new EPSG3857();
	private String id = "leafletmap" + count++;
	private MapOptions options;
	static private int count = 0;
	
	@Override
	public MapWidget getWidget() {
		return (MapWidget) super.getWidget();
	}

	@Override
	public LeafletMapState getState() {
		return (LeafletMapState) super.getState();
	}

	@Override
	protected Widget createWidget() {
		MapWidget mapWidget = new MapWidget(id);
		mapWidget.setHeight("100%");
		return mapWidget;
	}

	@Override
	protected void init() {
		super.init();
//		getLayoutManager().addElementResizeListener(getWidget().getElement(),
//				new ElementResizeListener() {
//					@Override
//					public void onElementResize(ElementResizeEvent e) {
//						map.invalidateSize(false);
//					}
//				});

	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
				if (map == null) {
					options = new MapOptions();
					if (getState().center != null) {
						options.setCenter(getCenterFromState());
					} else {
						options.setCenter(new LatLng(0, 0));
					}
					options.setProperty("crs", vCRS_EPSG3857);
					int zoom = 15;
					if (getState().zoomLevel != null) {
						zoom = getState().zoomLevel;
					}
					options.setZoom(zoom);
					map = new Map(id, options);

					addHardCodedTests();
					map.invalidateSize(false);

				} else {
					if (stateChangeEvent.getChangedProperties().contains(
							"center")
							&& getState().center != null) {
						LatLng center = getCenterFromState();
						map.setView(center, map.getZoom(), false);
					}
					if (stateChangeEvent.getChangedProperties().contains(
							"zoomLevel")
							&& getState().zoomLevel != null) {
						// TODO
					}
				}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				map.invalidateSize(false);
			}
		});
	}

	private LatLng getCenterFromState() {
		LatLng center = new LatLng(getState().center.getLat(),
				getState().center.getLon());
		return center;
	}

	/**
	 * This is just to add some test data during development. Copy pasted from
	 * gwtl-demo project.
	 */
	private void addHardCodedTests() {
		// Create TileLayer url template
		final String url = "http://{s}.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/997/256/{z}/{x}/{y}.png";

		// Create mutable TileLayer options
		Options tileOptions = new Options();
		tileOptions
				.setProperty(
						"attribution",
						"Map data &copy; 2011 OpenStreetMap contributors, Imagery &copy; 2011 CloudMade");

		// Create TileLayer instance
		TileLayer tile = new TileLayer(url, tileOptions);


		// Create layer switcher control
		Options bases = new Options();
		bases.setProperty("Tile", tile);

		Options overlays = new Options();
		// overlays.setProperty("Test",tile);

		ControlOptions controlOptions = new ControlOptions();
		controlOptions.setPosition(Position.BOTTOM_RIGHT);

//		// LayerGroup
//		LatLng glatlng1 = new LatLng(59.920, 10.754);
//		LatLng glatlng2 = new LatLng(59.922, 10.750);
//		LatLng glatlng3 = new LatLng(59.924, 10.752);
//		LatLng glatlng4 = new LatLng(59.926, 10.756);
//
//		MarkerOptions opt1 = new MarkerOptions();
//		opt1.setTitle("marker1");
//		MarkerOptions opt2 = new MarkerOptions();
//		opt2.setTitle("marker2");
//		MarkerOptions opt3 = new MarkerOptions();
//		opt3.setTitle("marker3");
//		MarkerOptions opt4 = new MarkerOptions();
//		opt4.setTitle("marker4");
//		Marker marker1 = new Marker(glatlng1, opt1);
//		Marker marker2 = new Marker(glatlng2, opt2);
//		Marker marker3 = new Marker(glatlng3, opt3);
//		Marker marker4 = new Marker(glatlng4, opt4);
//
//		Marker[] markers = new Marker[] { marker1, marker2 };
//		LayerGroup groupMarkers1 = new LayerGroup(markers);
//
//		Marker[] markers2 = new Marker[] { marker3, marker4 };
//		LayerGroup groupMarkers2 = new LayerGroup(markers2);
//		overlays.setProperty("Group marker 1", groupMarkers1);
//		overlays.setProperty("Group marker 2", groupMarkers2);

		// Add layers control to map
		Layers control = new Layers(bases, overlays, controlOptions);
		control.addTo(map);

//		// Create map center position
//		IconOptions iconOptions = new IconOptions();
//
//		iconOptions.setIconUrl(GWT.getHostPageBaseURL()
//				+ "images/icon-info.png");
//		iconOptions.setIconSize(new Point(32, 37));
//		iconOptions.setIconAnchor(new Point(16, 37));
//		iconOptions.setPopupAnchor(new Point(0, -28));
		// Icon icon = new Icon(iconOptions);

		// TODO Solve iconurl problem
		// loptions.setProperty("icon", icon);

//		final LatLng latlng = new LatLng(59.915, 10.754);
//		final Marker marker = new Marker(latlng, options);
//		marker.addTo(map);
//		marker.bindPopup("<b>Here is a simple popup<b>");

		// Add layers to map and center at given position
//		map.setView(latlng, 2, false);
		map.addLayer(tile);

//		LatLng latlng1 = new LatLng(59.915, 10.759);
//		LatLng latlng2 = new LatLng(59.900, 10.800);
//		LatLng latlng3 = new LatLng(59.990, 10.800);
//		LatLng[] latlngs = new LatLng[] { latlng1, latlng2, latlng3 };
//		GWT.log("size   :" + latlngs.length);
//		for (LatLng l : latlngs) {
//			GWT.log("string :" + l.toString());
//		}
//		GWT.log("Polyline");
//		Polyline poly = new Polyline(latlngs, new PolylineOptions());
//		poly.addTo(map);
//
//		// Circle
//		GWT.log("Circle");
//		Options circleOptions = new Options();
//		circleOptions.setProperty("color", "red");
//		Circle circle = new Circle(latlng, 200, circleOptions);
//		circle.addTo(map);
//
//		// Rectangle
//		GWT.log("Rectangle");
//		LatLng rec1 = new LatLng(59.900, 10.705);
//		LatLng rec2 = new LatLng(59.910, 10.710);
//		LatLng[] recs = new LatLng[] { rec1, rec2 };
//		LatLngBounds bounds = new LatLngBounds(recs);
//		Rectangle rec = new Rectangle(bounds, new PathOptions());
//		rec.addTo(map);
//		// map.fitBounds(bounds);
//
//		// Rectangle
//		GWT.log("Polygon editing");
//		final LatLng rec3 = new LatLng(59.910, 10.715);
//		final LatLng rec4 = new LatLng(59.915, 10.715);
//		final LatLng[] recs2 = new LatLng[] { rec1, rec2, rec3, rec4 };
//		final PolylineOptions pathOptions = new PolylineOptions();
//		pathOptions.setEditable(true);
//		pathOptions.setColor("yellow");
//		final Polygon pol = new Polygon(recs2, pathOptions);
//		pol.addTo(map);
//
//		// whne click on map change editing
//		final EventRegisteredFunction clickeRegistered = EventHandlerManager
//				.addEventHandler(map, Events.click,
//						new EventHandler<MouseEvent>() {
//
//							@Override
//							public void handle(MouseEvent event) {
//								GWT.log("Clicked on map:" + event.getLatLng());
//								map.fitBounds(pol.getBounds());
//								if (pol.editing().enabled()) {
//									pol.editing().disable();
//								} else {
//									pol.editing().enable();
//								}
//								EventHandlerManager.clearEventHandler(map,
//										Events.click);
//							}
//						});

		// Add Scale Control
		GWT.log("Scale Control");
		ScaleOptions scaleOptions = new ScaleOptions();
		Scale scale = new Scale(scaleOptions);
		scale.addTo(map);

		// Add Zoom Control
		GWT.log("Zoom Control");
		ZoomOptions zoomOptions = new ZoomOptions();
		zoomOptions.setPosition(Position.TOP_RIGHT);
		Zoom zoom = new Zoom(zoomOptions);
		zoom.addTo(map);

	}

	public Map getMap() {
		return map;
	}

	@Override
	public void updateCaption(ComponentConnector connector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectorHierarchyChange(
			ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
		// TODO Auto-generated method stub
		
	}


}
