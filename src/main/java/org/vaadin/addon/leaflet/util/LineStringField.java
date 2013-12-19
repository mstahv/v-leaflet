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

public class LineStringField extends AbstractJTSField<LineString> {

	private LPolyline lPolyline;

	public LineStringField() {
	}

	public LineStringField(String caption) {
		this();
		setCaption(caption);
	}

	@Override
	public Class<? extends LineString> getType() {
		return LineString.class;
	}

	protected void prepareEditing() {
		if (lPolyline == null) {
			lPolyline = new LPolyline();
			map.addLayer(lPolyline);
		}
		Point[] lPointArray = JTSUtil.toLeafletPointArray(getCrsTranslator()
				.toPresentation(getInternalValue()));
		lPolyline.setPoints(lPointArray);
		LEditing editing = new LEditing(lPolyline);
		editing.addFeatureModifiedListener(new FeatureModifiedListener() {

			@Override
			public void featureModified(FeatureModifiedEvent event) {
				setValue(getCrsTranslator().toModel(
						JTSUtil.toLineString(lPolyline)));
			}
		});
		map.zoomToExtent(new Bounds(lPolyline.getPoints()));
	}

	protected void prepareDrawing() {
		LDrawPolyline drawPolyline = new LDrawPolyline(map);
		drawPolyline.addFeatureDrawnListener(new FeatureDrawnListener() {

			@Override
			public void featureDrawn(FeatureDrawnEvent event) {
				setValue(getCrsTranslator().toModel(
						JTSUtil.toLineString((LPolyline) event
								.getDrawnFeature())));
			}
		});

	}

}
