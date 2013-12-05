package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.LeafletMapClientRpc;
import org.vaadin.addon.leaflet.client.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.client.LeafletMapState;
import org.vaadin.addon.leaflet.control.AbstractControl;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vaadin.server.Extension;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 */
public class LMap extends AbstractComponentContainer {

	private boolean rendered = false;

	private List<Component> components = new ArrayList<Component>();

	public LMap() {
		setSizeFull();
		registerRpc(new LeafletMapServerRpc() {
			@Override
			public void onClick(Point p) {
				fireEvent(new LeafletClickEvent(LMap.this, p));
			}

			@Override
			public void onMoveEnd(Bounds bounds, int zoomlevel) {
				getState(false).zoomLevel = zoomlevel;
				getState(false).center = bounds.getCenter();
				fireEvent(new LeafletMoveEndEvent(LMap.this, bounds, zoomlevel));
			}
		});

	}

	@Override
	public void beforeClientResponse(boolean initial) {
		rendered = true;
		super.beforeClientResponse(initial);
	}

	@Override
	public void detach() {
		super.detach();
		rendered = false;
	}

	@Override
	protected LeafletMapState getState(boolean markAsDirty) {
		return (LeafletMapState) super.getState(markAsDirty);
	}

	@Override
	protected LeafletMapState getState() {
		return (LeafletMapState) super.getState();
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Add a base layer with given name.
	 * 
	 * @param baseLayer
	 * @param name
	 *            to be used in LayerControl, null if layer should not be
	 *            displayed in the layer control
	 */
	public void addBaseLayer(LeafletLayer baseLayer, String name) {
		addLayer(baseLayer);
		LLayers control = getLayersControl();
		if (control != null) {
			control.addBaseLayer(baseLayer, name);
		}
	}

	public LLayers getLayersControl() {
		for (Extension e : getExtensions()) {
			if (e instanceof LLayers) {
				return (LLayers) e;
			}
		}
		LLayers lLayers = new LLayers();
		addExtension(lLayers);
		return lLayers;
	}

	public void addControl(AbstractControl control) {
		addExtension(control);
	}

	public void removeControl(AbstractControl control) {
		control.remove();
	}

	public void addOverlay(LeafletLayer overlay, String name) {
		addLayer(overlay);
		LLayers control = getLayersControl();
		if (control != null) {
			control.addOverlay(overlay, name);
		}
	}

	public void addLayer(LeafletLayer layer) {
		addComponent(layer);
	}

	@Override
	public void addComponent(Component c) {
		if (!(c instanceof LeafletLayer)) {
			throw new IllegalArgumentException(
					"only instances of LeafletLayer (AbstractLeafletLayer or LLayerGroup) allowed");
		}
		super.addComponent(c);
		components.add(c);
		markAsDirty(); // ?? is this really needed
	}

	@Override
	public void removeComponent(Component c) {
		super.removeComponent(c);
		components.remove(c);
		LLayers layersControl = getLayersControl();
		if (layersControl != null) {
			layersControl.removeLayer((LeafletLayer) c);
		}
		markAsDirty(); // ?? is this really needed
	}

	@Override
	public int getComponentCount() {
		return components.size();
	}

	public boolean hasComponent(Component component) {
		return components.contains(component);
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}

	public void setCenter(double lat, double lon) {
		Point point = new Point();
		point.setLat(lat);
		point.setLon(lon);
		setCenter(point);
	}

	public void setCenter(Point center) {
		getState(!rendered).center = center;
		if (rendered) {
			getRpcProxy(LeafletMapClientRpc.class).setCenter(center.getLat(),
					center.getLon(), null);
		}
	}

	public void setCenter(com.vividsolutions.jts.geom.Point jtsPoint) {
		Point point = new Point();
		point.setLat(jtsPoint.getY());
		point.setLon(jtsPoint.getX());
		setCenter(point);
	}

	public void setZoomLevel(int zoomLevel) {
		getState(!rendered).zoomLevel = zoomLevel;
		if (rendered) {
			getRpcProxy(LeafletMapClientRpc.class).setCenter(null, null,
					zoomLevel);
		}
	}

	public void addClickListener(LeafletClickListener listener) {
		addListener("click", LeafletClickEvent.class, listener,
				LeafletClickListener.METHOD);
	}

	public void removeClickListener(LeafletClickListener listener) {
		removeListener("click", LeafletClickEvent.class, listener);
	}

	public void addMoveEndListener(LeafletMoveEndListener moveEndListener) {
		addListener("moveend", LeafletMoveEndEvent.class, moveEndListener,
				LeafletMoveEndListener.METHOD);
	}

	public void removeMoveEndListener(LeafletMoveEndListener moveEndListener) {
		removeListener("moveend", LeafletMoveEndEvent.class, moveEndListener);
	}

	public void setCenter(Bounds bounds) {
		setCenter(bounds.getCenter());
	}

	public Integer getZoomLevel() {
		return getState().zoomLevel;
	}

	public void zoomToExtent(Bounds bounds) {
		getState(!rendered).center = bounds.getCenter();
		getState(!rendered).zoomToExtent = bounds;
		if (rendered) {
			// If already on the screen, use RPC instead to avoid "full paint"
			getRpcProxy(LeafletMapClientRpc.class).zoomToExtent(bounds);
		}
	}

	public void zoomToExtent(Geometry geometry) {
		Bounds bounds = JTSUtil.getBounds(geometry);
		zoomToExtent(bounds);
	}
	
	/**
	 * Calculates extent of all contained components that are not "baselayers".
	 */
	public void zoomToContent() {
		Collection<Geometry> gc = new ArrayList<Geometry>();
		for (Component c : this) {
			LeafletLayer l = (LeafletLayer) c;
			Geometry geometry = l.getGeometry();
			if(geometry != null) {
				gc.add(geometry);
			}
		}
		zoomToExtent(new GeometryFactory().buildGeometry(gc));
	}

	/**
	 * @param values
	 * @deprecated, use addControl() instead
	 */
	@Deprecated
	public void setControls(List<Control> values) {
		if (values != null) {
			for (Control control : values) {
				switch (control) {
				case zoom:
					addControl(new LZoom());
					break;
				case scale:
					addControl(new LScale());
				default:
					break;
				}
			}
		}
	}

	public Point getCenter() {
		return getState(false).center;
	}

	public void setAttributionPrefix(String prefix) {
		getState().attributionPrefix = prefix;
	}

	public String getAttributionPrefix() {
		return getState(false).attributionPrefix;
	}

}
