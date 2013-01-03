package org.vaadin.addon.leaflet;

import java.lang.reflect.Method;

import org.vaadin.addon.leaflet.client.vaadin.AbstractLeafletComponentState;
import org.vaadin.addon.leaflet.client.vaadin.ClickServerRpc;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.util.ReflectTools;

public abstract class LeafletLayer extends AbstractComponent {
	
	public LeafletLayer() {
		registerRpc(new ClickServerRpc() {
			@Override
			public void onClick() {
				fireEvent(new LeafletClickEvent(LeafletLayer.this));
			}
		});
	}
	
	public static class LeafletClickEvent extends ConnectorEvent {
		public LeafletClickEvent(ClientConnector source) {
			super(source);
		}
		
	}
	
	@Override
	protected AbstractLeafletComponentState getState() {
		return (AbstractLeafletComponentState) super.getState();
	}
	
	public interface LeafletClickListener {
		
		static final Method METHOD = ReflectTools.findMethod(LeafletClickListener.class, "onClick", LeafletClickEvent.class);
		
		void onClick(LeafletClickEvent event);
	}
	
	public void addClickListener(LeafletClickListener listener) {
		addListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}
	
	public void removeMarkerClickListener(LeafletClickListener listener) {
		removeListener(LeafletClickEvent.class, listener, LeafletClickListener.METHOD);
	}
}
