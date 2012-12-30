package org.vaadin.addon.leaflet.client.vaadin;

public class Point {
	private Double lon;
	private Double lat;
	
	public Point() {
	}
	
	public Point(double lon2, double lat2) {
		lon = lon2;
		lat = lat2;
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

}
