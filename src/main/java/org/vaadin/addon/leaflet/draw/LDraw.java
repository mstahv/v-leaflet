package org.vaadin.addon.leaflet.draw;

import java.lang.reflect.Method;
import java.util.EventObject;

import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawServerRcp;
import org.vaadin.addon.leaflet.client.vaadin.LeafletDrawState;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.util.ReflectTools;

public class LDraw extends AbstractControl {

	public static class FeatureDrawnEvent extends EventObject {
		private LeafletLayer drawnLayer;

		public FeatureDrawnEvent(LDraw lDraw, LeafletLayer drawnLayer) {
			super(lDraw);
			this.drawnLayer = drawnLayer;
		}

		public LeafletLayer getDrawnFeature() {
			return drawnLayer;
		}
	}

	public interface FeatureDrawnListener {
		public void featureDrawn(FeatureDrawnEvent event);
	}

	public interface FeaturesModifiedListener {

	}

	private static final Method addedMethod = ReflectTools
			.findMethod(FeatureDrawnListener.class, "featureDrawn",
					FeatureDrawnEvent.class);

	public void addFeatureDrawnListener(FeatureDrawnListener listener) {
		addListener(FeatureDrawnEvent.class, listener, addedMethod);
	}
	
	public void removeFeatureDrawnListener(FeatureDrawnListener listener) {
		removeListener(FeatureDrawnEvent.class, listener);
	}

	public LDraw() {
		registerRpc(new LeafletDrawServerRcp() {
			@Override
			public void markerDrawn(Point p) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LMarker(p)));
			}

			@Override
			public void circleDrawn(Point point, double radius) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LCircle(point, radius)));
			}

			@Override
			public void polygonDrawn(Point[] latLngs) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LPolygon(latLngs)));
			}

			@Override
			public void polylineDrawn(Point[] latLngs) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LPolyline(latLngs)));
			}
		});
	}

	@Override
	protected LeafletDrawState getState() {
		return (LeafletDrawState) super.getState();
	}

	public void setEditableFeatureGroup(LFeatureGroup group) {
		getState().featureGroup = group;
	}

}
