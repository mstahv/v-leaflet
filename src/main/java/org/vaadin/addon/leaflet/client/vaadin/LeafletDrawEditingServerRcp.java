package org.vaadin.addon.leaflet.client.vaadin;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface LeafletDrawEditingServerRcp extends ServerRpc {

	@Delayed(lastOnly=true)
	public void circleModified(Connector cc, Point latLng,
			double radius);

	@Delayed(lastOnly=true)
	public void polylineModified(Connector plc,
			Point[] pointArray);

}
