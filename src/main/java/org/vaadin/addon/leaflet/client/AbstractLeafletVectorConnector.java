package org.vaadin.addon.leaflet.client;

import org.peimari.gleaflet.client.AbstractPath;
import org.peimari.gleaflet.client.PathOptions;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ServerConnector;

public abstract class AbstractLeafletVectorConnector<T extends AbstractLeafletVectorState, O extends PathOptions>
		extends AbstractLeafletLayerConnector<O> {

	AbstractLeafletVectorConnector() {
		registerRpc(LeafletMarkerClientRpc.class, new LeafletMarkerClientRpc() {

			@Override
			public void openPopup() {
				Scheduler.get().scheduleDeferred(
						new Scheduler.ScheduledCommand() {

							@Override
							public void execute() {
								getVector().openPopup();
							}
						});
			}

			@Override
			public void closePopup() {
				getVector().closePopup();
			}

		});

	}

	protected AbstractPath getVector() {
		return (AbstractPath) getLayer();
	}

	@Override
	protected O createOptions() {
		O o = (O) O.create();
		AbstractLeafletVectorState s = getState();
		if (s.getVectorStyle().getColor() != null) {
			o.setColor(s.getVectorStyle().getColor());
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

		final String popup = getState().popup;
		if (popup != null) {
			Scheduler.get().scheduleFinally(new ScheduledCommand() {

				@Override
				public void execute() {
					getVector().bindPopup(
							popup,
							LeafletMarkerConnector
									.popupOptionsFor(getState().popupState));
				}
			});

		}

		return o;
	}
	
	@Override
	public void setParent(ServerConnector parent) {
		super.setParent(parent);
		markDirty();
	}

	@Override
	public T getState() {
		return (T) super.getState();
	}
}