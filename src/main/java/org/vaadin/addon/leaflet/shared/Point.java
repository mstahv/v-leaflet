package org.vaadin.addon.leaflet.shared;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable {
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

	@JsonValue
	public Double[] getLatLonPair() {
		return new Double[]{lat,lon};
	}

    @Override
    public int hashCode() {
        int hash = 3;
        return hash*(lon == null ? 0 : lon.intValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (!Objects.equals(this.lon, other.lon)) {
            return false;
        }
        if (!Objects.equals(this.lat, other.lat)) {
            return false;
        }
        return true;
    }
    
    

}
