package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawServerRcp;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawState;
import org.vaadin.addon.leaflet.client.vaadin.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

public class LDraw extends AbstractControl {

	public LDraw() {
		registerRpc(new LeafletDrawServerRcp() {
			
			@Override
			public void markerDrawn(Point p) {
				// TODO
				System.err.println(p);
			}
		});

		// TODO Auto-generated constructor stub
	}

	@Override
	protected LeafletDrawState getState() {
		return (LeafletDrawState) super.getState();
	}

	public void setEditableFeatureGroup(LFeatureGroup group) {
		getState().featureGroup = group;
	}

}
