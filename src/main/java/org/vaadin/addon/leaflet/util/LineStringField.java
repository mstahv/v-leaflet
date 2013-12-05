package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedListener;
import org.vaadin.addon.leaflet.draw.LDrawPolyline;
import org.vaadin.addon.leaflet.draw.LEditing;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.LineString;

public class LineStringField extends JTSField<LineString> {

	private LPolyline lPolyline;

	@Override
	public Class<? extends LineString> getType() {
		return LineString.class;
	}

	protected void prapareEditing() {
		if(lPolyline == null ) {
			lPolyline = new LPolyline();
			map.addLayer(lPolyline);
		}
		Point[] lPointArray = JTSUtil.toLeafletPointArray(getInternalValue());
		lPolyline.setPoints(lPointArray);
		LEditing editing = new LEditing(lPolyline);
		editing.addFeatureModifiedListener(new FeatureModifiedListener() {

			@Override
			public void featureModified(FeatureModifiedEvent event) {
				setValue(JTSUtil.toLineString(lPolyline));
			}
		});
		map.zoomToExtent(new Bounds(lPolyline.getPoints()));
	}

	protected void prepareDrawing() {
		LDrawPolyline drawPolyline = new LDrawPolyline(map);
		drawPolyline.addFeatureDrawnListener(new FeatureDrawnListener() {
			
			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				setValue(JTSUtil.toLineString((LPolyline)event.getDrawnFeature()));
			}
		});

	}

}
