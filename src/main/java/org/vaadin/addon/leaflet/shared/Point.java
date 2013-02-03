package org.vaadin.addon.leaflet.shared;

public class Point {
	private Double lon;
	private Double lat;
	
	public Point() {
	}
	
	public Point(double lat, double lon) {
		this.lon = lon;
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	@Override
	public String toString() {
		return lat + "," + lon;
	}

}
