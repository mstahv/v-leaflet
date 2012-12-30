package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMarkerState;
import org.vaadin.addon.leaflet.client.vaadin.MarkerServerRpc;
import org.vaadin.addon.leaflet.client.vaadin.Point;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.util.ReflectTools;

/**
 * Prototype. This might be technically easier to implement as an extension with Leaflet, but
 * I'm trying this way to pave way for OL integration (which has vectorlayer,
 * extensions cannot have children -> needs to be componentcontainer)
 * 
 */
public class LeafletMarker extends AbstractComponent {

	public static class MarkerClickEvent extends ConnectorEvent {
		public MarkerClickEvent(LeafletMarker source) {
			super(source);
		}
		
	}
	
	public interface MarkerClickListener {
		
		static final Method METHOD = ReflectTools.findMethod(MarkerClickListener.class, "onClick", MarkerClickEvent.class);
		
		void onClick(MarkerClickEvent event);
	}
	
	public void addMarkerClickListener(MarkerClickListener listener) {
		addListener(MarkerClickEvent.class, listener, MarkerClickListener.METHOD);
	}
	
	public void removeMarkerClickListener(MarkerClickListener listener) {
		removeListener(MarkerClickEvent.class, listener, MarkerClickListener.METHOD);
	}

	@Override
	protected LeafletMarkerState getState() {
		return (LeafletMarkerState) super.getState();
	}

	public LeafletMarker(double lat, double lon) {
		registerRpc(new MarkerServerRpc() {
			@Override
			public void onClick() {
				fireEvent(new MarkerClickEvent(LeafletMarker.this));
			}
		});
		getState().point = new Point(lon, lat);
	}

}
