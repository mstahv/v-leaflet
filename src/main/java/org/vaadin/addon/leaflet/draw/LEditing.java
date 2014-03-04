package org.vaadin.addon.leaflet.draw;

import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedListener;
import org.vaadin.addon.leaflet.draw.client.LeafletDrawEditingServerRcp;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.AbstractExtension;
import com.vaadin.shared.Connector;

/**
 * Editing extension for Leaflet vectors.
 * <p>
 * Can be used instead of the {@link LDraw} to edit just a specific layer. The
 * constructor adds the extension to {@link AbstractLeafletVector}. The
 * extension is automatically removed after first edit event or on removal.
 * 
 */
public class LEditing extends AbstractExtension {

	public void addFeatureModifiedListener(FeatureModifiedListener listener) {
		addListener(FeatureModifiedEvent.class, listener,
				FeatureModifiedListener.modifiedMethod);
	}

	public void removeFeatureModifiedListener(FeatureModifiedListener listener) {
		removeListener(FeatureModifiedEvent.class, listener);
	}

	public LEditing(AbstractLeafletVector vector) {
		extend(vector);
		registerRpc();
	}

	private void registerRpc() {
		registerRpc(new LeafletDrawEditingServerRcp() {

			@Override
			public void circleModified(Connector cc, Point latLng, double radius) {
				LCircle c = (LCircle) cc;
				c.setRadius(radius);
				c.setPoint(latLng);
				fireEvent(new FeatureModifiedEvent(LEditing.this, c));
				remove();
			}

			@Override
			public void polylineModified(Connector plc, Point[] pointArray) {
				LPolyline pl = (LPolyline) plc;
				pl.setPoints(pointArray);
				fireEvent(new FeatureModifiedEvent(LEditing.this, pl));
				remove();
			}

		});
	}

	@Override
	public void remove() {
		if (getParent() != null) {
			super.remove();
		}
	}

}
