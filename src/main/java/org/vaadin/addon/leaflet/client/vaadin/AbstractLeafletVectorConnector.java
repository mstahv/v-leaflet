package org.vaadin.addon.leaflet.client.vaadin;


import org.peimari.gleaflet.client.PathOptions;

public abstract class AbstractLeafletVectorConnector<T extends AbstractLeafletVectorState, O extends PathOptions>
        extends AbstractLeafletLayerConnector<O> {

	@Override
	protected O createOptions() {
 		O o = (O) O.create();
		AbstractLeafletVectorState s = getState();
		if (s.stroke != null) {
			o.setStroke(s.stroke);
		}
		if (s.fill != null) {
			o.setFill(s.fill);
		}
		if (s.fillColor != null) {
			o.setFillColor(s.fillColor);
		}
		if (s.weight != null) {
			o.setWeight(s.weight);
		}
		if (s.opacity != null) {
			o.setOpacity(s.opacity);
		}
		if (s.dashArray != null) {
			o.setDashArray(s.dashArray);
		}
		if (s.lineCap != null) {
			o.setLineCap(s.lineCap);
		}
		if (s.lineJoin != null) {
			o.setLineJoin(s.lineJoin);
		}
		if (s.clickable != null) {
			o.setClickable(s.clickable);
		}
		if (s.pointerEvents != null) {
			o.setPointerEvents(s.pointerEvents);
		}
		if (s.className != null) {
			o.setClassName(s.className);
		}
		return o;
	}

	@Override
	public AbstractLeafletVectorState getState() {
		return (AbstractLeafletVectorState) super.getState();
	}
}