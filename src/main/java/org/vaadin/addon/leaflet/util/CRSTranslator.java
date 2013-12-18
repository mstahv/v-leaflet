package org.vaadin.addon.leaflet.util;

import com.vividsolutions.jts.geom.Geometry;

public interface CRSTranslator<T extends Geometry> {

	/**
	 * Converts a geometry to a CRS suitable for Leaflet (WGS84 aka
	 * EPSG:4326)
	 * 
	 * @param geom
	 * @return geometry transformed to EPSG:4326
	 */
	T toPresentation(T geom);

	/**
	 * Converts geometry coming from V-Leaflet (in WGS84 aka EPSG:4326) to
	 * another CRS for model.
	 * 
	 * @param geom
	 * @return
	 */
	T toModel(T geom);
}