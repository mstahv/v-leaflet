package org.vaadin.addon.leaflet;

import com.vaadin.event.ConnectorEvent;
import com.vaadin.server.ClientConnector;

public class LeafletMoveEndEvent extends ConnectorEvent {

	private double southWestLng;
	private double southWestLat;
	private double northEastLng;
	private double northEastLat;

	public LeafletMoveEndEvent(ClientConnector source, String bounds) {
		super(source);
		
		String[] split = bounds.split(",");
		setSouthWestLng(Double.parseDouble(split[0]));
		setSouthWestLat(Double.parseDouble(split[1]));
		setNorthEastLng(Double.parseDouble(split[2]));
		setNorthEastLat(Double.parseDouble(split[3]));
		
	}

	public double getSouthWestLng() {
		return southWestLng;
	}

	public void setSouthWestLng(double southWestLng) {
		this.southWestLng = southWestLng;
	}

	public double getSouthWestLat() {
		return southWestLat;
	}

	public void setSouthWestLat(double southWestLat) {
		this.southWestLat = southWestLat;
	}

	public double getNorthEastLng() {
		return northEastLng;
	}

	public void setNorthEastLng(double northEastLng) {
		this.northEastLng = northEastLng;
	}

	public double getNorthEastLat() {
		return northEastLat;
	}

	public void setNorthEastLat(double northEastLat) {
		this.northEastLat = northEastLat;
	}
	
}