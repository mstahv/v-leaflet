package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMapState;
import org.vaadin.addon.leaflet.client.vaadin.Point;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * 
 */
public class LeafletMap extends AbstractComponentContainer {
	
	private List<Component> components = new ArrayList<Component>();

	public LeafletMap() {		
		setSizeFull();
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
	}
	

	@Override
	protected LeafletMapState getState() {
		return (LeafletMapState) super.getState();
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addComponent(Component c) {
		super.addComponent(c);
		components.add(c);
	}

	@Override
	public int getComponentCount() {
		return components.size();
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}

	public void setCenter(double lat, double lon) {
		Point point = new Point();
		point.setLat(lat);
		point.setLon(lon);
		getState().center = point;
		
	}

	public void setZoomLevel(int zoomLevel) {
		getState().zoomLevel = zoomLevel;
	}

}
