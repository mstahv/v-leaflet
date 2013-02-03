package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.client.vaadin.LeafletMapState;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * 
 */
public class LeafletMap extends AbstractComponentContainer {
	
	private List<Component> components = new ArrayList<Component>();

	public LeafletMap() {		
		setSizeFull();
		registerRpc(new LeafletMapServerRpc() {
			@Override
			public void onClick(Point p) {
				fireEvent(new LeafletClickEvent(LeafletMap.this, p));
			}

			@Override
			public void onMoveEnd(String bBoxString) {
				fireEvent(new LeafletMoveEndEvent(LeafletMap.this, bBoxString));
			}
		});

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
	
	public BaseLayer[] getBaseLayers() {
		return getState().getBaseLayers();
	}
	
	public void setBaseLayers(BaseLayer... baselayer) {
		getState().setBaseLayers(baselayer);
	}

	public void addClickListener(LeafletClickListener listener) {
		addListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}
	
	public void removeMarkerClickListener(LeafletClickListener listener) {
		removeListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}

	public void addMoveEndListener(LeafletMoveEndListener moveEndListener) {
		addListener(LeafletMoveEndEvent.class, moveEndListener, LeafletMoveEndListener.METHOD);
	}

	
	public void removeMoveEndListener(LeafletMoveEndListener moveEndListener) {
		removeListener(LeafletMoveEndEvent.class, moveEndListener, LeafletMoveEndListener.METHOD);
	}

}
