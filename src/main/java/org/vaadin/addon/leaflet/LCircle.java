package org.vaadin.addon.leaflet;

import org.vaadin.addon.leaflet.client.LeafletCircleState;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.util.JTSUtil;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A class for drawing circle overlays on a map.
 */
public class LCircle extends AbstractLeafletVector {

	@Override
	protected LeafletCircleState getState() {
		return (LeafletCircleState) super.getState();
	}

	public LCircle(double lat, double lon, double radius) {
		getState().point = new Point(lat, lon);
		setRadius(radius);
	}

	public LCircle(Point point, double radius) {
		setPoint(point);
		setRadius(radius);
	}

	public LCircle(com.vividsolutions.jts.geom.Point jtsPoint, double radius) {
		this(JTSUtil.toLeafletPoint(jtsPoint), radius);
	}

	public void setPoint(Point point) {
		getState().point = point;
	}

	public void setRadius(double radius) {
		getState().radius = radius;
	}

	public Point getPoint() {
		return getState().point;
	}

	public double getRadius() {
		return getState().radius;
	}

	@Override
	public Geometry getGeometry() {
		return JTSUtil.toPoint(getPoint());
	}

}
