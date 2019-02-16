package org.vaadin.addon.leaflet;

import com.vaadin.ui.Component;
import org.locationtech.jts.geom.Geometry;

/**
 * Marker interface to be implemented by all server side counterparts for
 * Leaflets ILayer interface.
 */
public interface LeafletLayer extends Component {

	/**
	 * @return The geometry of the layer or null if the layer covers everything
	 *         (e.g. various tile layers).
	 */
	Geometry getGeometry();
    
    void bringToFront();
    void bringToBack();

}
