package org.vaadin.addon.leaflet.draw;

import java.lang.reflect.Method;
import java.util.EventObject;

import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LFeatureGroup;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.draw.client.LeafletDrawServerRcp;
import org.vaadin.addon.leaflet.draw.client.LeafletDrawState;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.shared.Connector;
import com.vaadin.util.ReflectTools;
import org.vaadin.addon.leaflet.LRectangle;
import org.vaadin.addon.leaflet.shared.Bounds;

/**
 * Draw "toolbar" that is added to the map. This allows users to draw and edit
 * various feature types.
 */
public class LDraw extends AbstractControl {

	public static class FeatureDrawnEvent extends EventObject {
		private LeafletLayer drawnLayer;

		public FeatureDrawnEvent(Connector lDraw, LeafletLayer drawnLayer) {
			super(lDraw);
			this.drawnLayer = drawnLayer;
		}

		public LeafletLayer getDrawnFeature() {
			return drawnLayer;
		}
	}

	public static class FeatureModifiedEvent extends EventObject {
		private LeafletLayer modifiedLayer;

		public FeatureModifiedEvent(AbstractClientConnector connector,
				LeafletLayer modifiedLayer) {
			super(connector);
			this.modifiedLayer = modifiedLayer;
		}

		public LeafletLayer getModifiedFeature() {
			return modifiedLayer;
		}
	}

	public static class FeatureDeletedEvent extends EventObject {
		private LeafletLayer deleted;

		public FeatureDeletedEvent(LDraw lDraw, LeafletLayer deletedLayer) {
			super(lDraw);
			this.deleted = deletedLayer;
		}

		public LeafletLayer getDeletedFeature() {
			return deleted;
		}
	}

	public interface FeatureDrawnListener {
		public static final Method drawnMethod = ReflectTools
				.findMethod(FeatureDrawnListener.class, "featureDrawn",
						FeatureDrawnEvent.class);

		public void featureDrawn(FeatureDrawnEvent event);
	}

	public interface FeatureModifiedListener {
		public static final Method modifiedMethod = ReflectTools.findMethod(
				FeatureModifiedListener.class, "featureModified",
				FeatureModifiedEvent.class);

		public void featureModified(FeatureModifiedEvent event);
	}

	public interface FeatureDeletedListener {
		public void featureDeleted(FeatureDeletedEvent event);
	}

	private static final Method deletedMethod = ReflectTools.findMethod(
			FeatureDeletedListener.class, "featureDeleted",
			FeatureDeletedEvent.class);


	public void addFeatureDrawnListener(FeatureDrawnListener listener) {
		addListener(FeatureDrawnEvent.class, listener, FeatureDrawnListener.drawnMethod);
	}

	public void removeFeatureDrawnListener(FeatureDrawnListener listener) {
		removeListener(FeatureDrawnEvent.class, listener);
	}

	public void addFeatureModifiedListener(FeatureModifiedListener listener) {
		addListener(FeatureModifiedEvent.class, listener,
				FeatureModifiedListener.modifiedMethod);
	}

	public void removeFeatureModifiedListener(FeatureModifiedListener listener) {
		removeListener(FeatureModifiedEvent.class, listener);
	}

	public void addFeatureDeletedListener(FeatureDeletedListener listener) {
		addListener(FeatureDeletedEvent.class, listener, deletedMethod);
	}

	public void removeFeatureDeletedListener(FeatureDeletedListener listener) {
		removeListener(FeatureDeletedEvent.class, listener);
	}

	public LDraw() {
		registerRpc(new LeafletDrawServerRcp() {
			@Override
			public void markerDrawn(Point p) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LMarker(p)));
			}

			@Override
			public void circleDrawn(Point point, double radius) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LCircle(point,
						radius)));
			}

                        @Override
                        public void rectangleDrawn(Bounds bounds) {
                                fireEvent(new FeatureDrawnEvent(LDraw.this, new LRectangle(
                                                bounds)));
                        }

			@Override
			public void polygonDrawn(Point[] latLngs) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LPolygon(
						latLngs)));
			}

			@Override
			public void polylineDrawn(Point[] latLngs) {
				fireEvent(new FeatureDrawnEvent(LDraw.this, new LPolyline(
						latLngs)));
			}

			@Override
			public void markerModified(Connector mc, Point newPoint) {
				LMarker m = (LMarker) mc;
				m.setPoint(newPoint);
				fireEvent(new FeatureModifiedEvent(LDraw.this, m));
			}

			@Override
			public void circleModified(Connector cc, Point latLng, double radius) {
				LCircle c = (LCircle) cc;
				c.setRadius(radius);
				c.setPoint(latLng);
				fireEvent(new FeatureModifiedEvent(LDraw.this, c));
			}

			@Override
			public void polylineModified(Connector plc, Point[] pointArray) {
				LPolyline pl = (LPolyline) plc;
				pl.setPoints(pointArray);
				fireEvent(new FeatureModifiedEvent(LDraw.this, pl));
			}

                        @Override
                        public void rectangleModified(Connector rc, Bounds bounds) {
                                    LRectangle r = (LRectangle) rc;
                                    r.setBounds(bounds);
                                    fireEvent(new FeatureModifiedEvent(LDraw.this, r));
                        }

			@Override
			public void layerDeleted(Connector c) {
				fireEvent(new FeatureDeletedEvent(LDraw.this, (LeafletLayer) c));
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
