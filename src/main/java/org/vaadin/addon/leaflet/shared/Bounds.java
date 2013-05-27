package org.vaadin.addon.leaflet.shared;

import java.io.Serializable;

public class Bounds implements Serializable {
	private double southWestLng;
	private double southWestLat;
	private double northEastLng;
	private double northEastLat;

	public Bounds() {
	}

	public Bounds(String bounds) {
		String[] split = bounds.split(",");
		setSouthWestLon(Double.parseDouble(split[0]));
		setSouthWestLat(Double.parseDouble(split[1]));
		setNorthEastLon(Double.parseDouble(split[2]));
		setNorthEastLat(Double.parseDouble(split[3]));
	}

	public Bounds(Point point) {
		setSouthWestLat(point.getLat());
		setSouthWestLon(point.getLon());
		setNorthEastLat(point.getLat());
		setNorthEastLon(point.getLon());
	}

	public Bounds(Point... points) {
		this(points[0]);
		for (Point point : points) {
			extend(point);
		}
	}

	public double getSouthWestLon() {
		return southWestLng;
	}

	public void setSouthWestLon(double southWestLng) {
		this.southWestLng = southWestLng;
	}

	public double getSouthWestLat() {
		return southWestLat;
	}

	public void setSouthWestLat(double southWestLat) {
		this.southWestLat = southWestLat;
	}

	public double getNorthEastLon() {
		return northEastLng;
	}

	public void setNorthEastLon(double northEastLng) {
		this.northEastLng = northEastLng;
	}

	public double getNorthEastLat() {
		return northEastLat;
	}

	public void setNorthEastLat(double northEastLat) {
		this.northEastLat = northEastLat;
	}

	public void extend(Point point) {
		setNorthEastLat(Math.max(getNorthEastLat(), point.getLat()));
		setNorthEastLon(Math.max(getNorthEastLon(), point.getLon()));
		setSouthWestLat(Math.min(getSouthWestLat(), point.getLat()));
		setSouthWestLon(Math.min(getSouthWestLon(), point.getLon()));

	}

	public Point getCenter() {
		return new Point((getNorthEastLat() + getSouthWestLat()) / 2,
				(getNorthEastLon() + getSouthWestLon()) / 2);
	}

}
