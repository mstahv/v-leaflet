package org.vaadin.addon.leaflet.util;

import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureDrawnListener;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedEvent;
import org.vaadin.addon.leaflet.draw.LDraw.FeatureModifiedListener;
import org.vaadin.addon.leaflet.draw.LDrawPolygon;
import org.vaadin.addon.leaflet.draw.LEditing;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;

import com.vividsolutions.jts.geom.LinearRing;

public class LinearRingField extends AbstractJTSField<LinearRing> {

	private LPolygon lPolygon;

	public LinearRingField() {
	}

	public LinearRingField(String caption) {
		this();
		setCaption(caption);
	}

	@Override
	public Class<? extends LinearRing> getType() {
		return LinearRing.class;
	}

	protected void prapareEditing() {
		if (lPolygon == null) {
			lPolygon = new LPolygon();
			map.addLayer(lPolygon);
		}
		Point[] lPointArray = JTSUtil.toLeafletPointArray(getInternalValue());
		lPolygon.setPoints(lPointArray);
		LEditing editing = new LEditing(lPolygon);
		editing.addFeatureModifiedListener(new FeatureModifiedListener() {

			@Override
			public void featureModified(FeatureModifiedEvent event) {
				setValue(JTSUtil.toLinearRing(lPolygon));
			}
		});
		map.zoomToExtent(new Bounds(lPolygon.getPoints()));
	}

	protected void prepareDrawing() {
		LDrawPolygon drawPolyline = new LDrawPolygon(map);
		drawPolyline.addFeatureDrawnListener(new FeatureDrawnListener() {

			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				// TODO fill Vaadin bug report: exception from here has horrible
				// stack trace (non informative), even more horrible than the
				// usual that has some irrelevant stuff in front
				setValue(JTSUtil.toLinearRing((LPolygon) event
						.getDrawnFeature()));
			}
		});

	}

}
