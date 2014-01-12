package org.vaadin.addon.leaflet.client;


import org.peimari.gleaflet.client.PathOptions;

public abstract class AbstractLeafletVectorConnector<T extends AbstractLeafletVectorState, O extends PathOptions>
        extends AbstractLeafletLayerConnector<O> {

	@Override
	protected O createOptions() {
 		O o = (O) O.create();
		AbstractLeafletVectorState s = getState();
		if (s.color != null) {
			o.setColor(s.color);
		}
		if (s.getVectorStyle().getStroke() != null) {
			o.setStroke(s.getVectorStyle().getStroke());
		}
		if (s.getVectorStyle().getFill() != null) {
			o.setFill(s.getVectorStyle().getFill());
		}
		if (s.getVectorStyle().getFillColor() != null) {
			o.setFillColor(s.getVectorStyle().getFillColor());
		}
		if (s.getVectorStyle().getFillOpacity() != null) {
			o.setFillOpacity(s.getVectorStyle().getFillOpacity());
		}
		if (s.getVectorStyle().getWeight() != null) {
			o.setWeight(s.getVectorStyle().getWeight());
		}
		if (s.getVectorStyle().getOpacity() != null) {
			o.setOpacity(s.getVectorStyle().getOpacity());
		}
		if (s.getVectorStyle().getDashArray() != null) {
			o.setDashArray(s.getVectorStyle().getDashArray());
		}
		if (s.getVectorStyle().getLineCap() != null) {
			o.setLineCap(s.getVectorStyle().getLineCap());
		}
		if (s.getVectorStyle().getLineJoin() != null) {
			o.setLineJoin(s.getVectorStyle().getLineJoin());
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
	public T getState() {
		return (T) super.getState();
	}
}