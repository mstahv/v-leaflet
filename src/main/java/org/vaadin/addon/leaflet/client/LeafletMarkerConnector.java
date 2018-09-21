package org.vaadin.addon.leaflet.client;

import com.vaadin.shared.ui.ComponentStateUtil;
import org.vaadin.addon.leaflet.shared.PopupState;
import org.vaadin.addon.leaflet.shared.LeafletMarkerState;
import org.vaadin.addon.leaflet.shared.DragEndServerRpc;
import org.vaadin.addon.leaflet.shared.LeafletMarkerClientRpc;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.MouseEventDetailsBuilder;

import org.peimari.gleaflet.client.ClickListener;
import org.peimari.gleaflet.client.DivIcon;
import org.peimari.gleaflet.client.DivIconOptions;
import org.peimari.gleaflet.client.Layer;
import org.peimari.gleaflet.client.Icon;
import org.peimari.gleaflet.client.IconOptions;
import org.peimari.gleaflet.client.LatLng;
import org.peimari.gleaflet.client.Marker;
import org.peimari.gleaflet.client.MarkerOptions;
import org.peimari.gleaflet.client.MouseEvent;
import org.peimari.gleaflet.client.MouseOutListener;
import org.peimari.gleaflet.client.MouseOverListener;
import org.peimari.gleaflet.client.ContextMenuListener;
import org.peimari.gleaflet.client.Point;
import org.peimari.gleaflet.client.PopupOptions;
import org.peimari.gleaflet.client.TooltipOptions;
import org.vaadin.addon.leaflet.shared.EventId;

import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.addon.leaflet.LMarker.class)
public class LeafletMarkerConnector extends
        AbstractLeafletLayerConnector<MarkerOptions> {

    private Marker marker;

    LeafletMarkerClientRpc clientRpc = new LeafletMarkerClientRpc() {

        @Override
        public void openTooltip() {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    marker.openTooltip();
                }
            });
        }

        @Override
        public void closeTooltip() {
            marker.closeTooltip();
        }

        @Override
        public void openPopup() {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    marker.openPopup();
                }
            });
        }

        @Override
        public void closePopup() {
            marker.closePopup();
        }

    };

    DragEndServerRpc dragServerRcp = getRpcProxy(DragEndServerRpc.class);

    public LeafletMarkerConnector() {
        registerRpc(LeafletMarkerClientRpc.class, clientRpc);
    }

    @Override
    public LeafletMarkerState getState() {
        return (LeafletMarkerState) super.getState();
    }

    ClickListener handler = new ClickListener() {
        @Override
        public void onClick(MouseEvent event) {
            rpc.onClick(U.toPoint(event.getLatLng()),
                    MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getLeafletMapConnector().getWidget().getElement()));
        }
    };

    @Override
    protected void update() {
        if (marker != null) {
            removeLayerFromParent();
            marker.removeClickListener();
            marker.removeMouseOverListener();
            marker.removeMouseOutListener();
            marker.removeContextMenuListener();
        }
        LatLng latlng = LatLng.create(getState().point.getLat(),
                getState().point.getLon());
        MarkerOptions options = createOptions();

        URLReference urlReference = getState().resources.get("icon");
        String divIcon = getState().divIcon;
        boolean fontIconMarker = urlReference != null && urlReference.getURL().startsWith("fonticon://");
        if (fontIconMarker || getState().markerChar != null) {
            String pathFill = getState().iconPathFill != null ? getState().iconPathFill : "#44AEEA";
            String pathStroke = getState().iconPathStroke != null ? getState().iconPathStroke : "#005FA8";
            String textFill = getState().iconTextFill != null ? getState().iconTextFill : "#fff";

            com.vaadin.client.ui.Icon vIcon;
            String fontAwesomeChar = null;
            StringBuilder svgSb = new StringBuilder();
            // TODO make this configurable, consider making also possible to
            // use configurable SVG marker without fontawesome icon in marker
            svgSb.append("<svg width=\"25px\" height=\"40px\"><path fill=\"");
            svgSb.append(pathFill);
            svgSb.append("\" stroke=\"");
            svgSb.append(pathStroke);
            svgSb.append("\" d=\"M12.544,0.5C5.971,0.5,0.5,6.24,0.5,12.416c0,2.777,1.564,6.308,2.694,8.745\n"
                    + "L12.5,38.922l9.262-17.761c1.13-2.438,2.738-5.791,2.738-8.745C24.5,6.24,19.117,0.5,12.544,0.5L12.544,0.5z\"/>");
            svgSb.append("<text fill=\"");
            svgSb.append(textFill);
            svgSb.append("\" x=\"12.5\" y=\"20\" text-anchor=\"middle\" font-size=\"16\" class=\"");
            if(fontIconMarker) {
                vIcon = getIcon();
                fontAwesomeChar = vIcon.getElement().getInnerText();
                svgSb.append(vIcon.getStyleName());
            }
            svgSb.append("\">");
            if(fontIconMarker) {
                svgSb.append(fontAwesomeChar);
            } else {
                svgSb.append(getState().markerChar);
            }
            svgSb.append("</text></svg>");

            DivIconOptions divIconOptions = DivIconOptions.create();
            divIconOptions.setClassName("v-leaflet-custom-svg ");
            if(ComponentStateUtil.hasStyles(getState())) {
                divIconOptions.setClassName(divIconOptions.getClassName() + String.join(" ", getState().styles));
            }
            divIconOptions.setHtml(svgSb.toString());
            divIconOptions.setIconSize(Point.create(25, 40));
            divIconOptions.setIconAnchor(Point.create(12.5, 40));
            configureIconSize(divIconOptions);

            DivIcon icon = DivIcon.create(divIconOptions);
            options.setIcon(icon);

        } else if (divIcon != null) {
            DivIconOptions divIconOptions = DivIconOptions.create();
            configureIconSize(divIconOptions);
            if (ComponentStateUtil.hasStyles(getState())) {
                divIconOptions.setClassName(String.join(" ", getState().styles));
            }
            divIconOptions.setHtml(divIcon);
            DivIcon icon = DivIcon.create(divIconOptions);
            options.setIcon(icon);
        } else if (urlReference != null) {
            IconOptions iconOptions = IconOptions.create();
            iconOptions.setIconUrl(urlReference.getURL());
            if (getState().popupAnchor != null) {
                iconOptions.setPopupAnchor(Point.create(
                        getState().popupAnchor.getLat(),
                        getState().popupAnchor.getLon()));
            }
            if (getState().iconAnchor != null) {
                iconOptions.setIconAnchor(Point.create(
                        getState().iconAnchor.getLat(),
                        getState().iconAnchor.getLon()));
            }
            if (getState().iconSize != null) {
                iconOptions.setIconSize(Point.create(
                        getState().iconSize.getLat(),
                        getState().iconSize.getLon()));
            }
            if (ComponentStateUtil.hasStyles(getState())) {
                iconOptions.setClassName(String.join(" ", getState().styles));
            }
            Icon icon = Icon.create(iconOptions);
            options.setIcon(icon);
        }

        String title = getState().title;
        if (title != null) {
            options.setTitle(title);
        }

        if (hasEventListener("dragend")) {
            options.setDraggable(true);
        }
        
        Integer zIndexOffset = getState().zIndexOffset;
        if (zIndexOffset != null)
        {
            options.setZIndexOffset(zIndexOffset);
        }

        marker = Marker.create(latlng, options);
        if (hasEventListener("dragend")) {
            marker.addDragEndListener(new ClickListener() {
                @Override
                public void onClick(MouseEvent event) {
                    dragServerRcp.dragEnd(U.toPoint(marker.getLatLng()));
                }
            });
        }
        if (hasEventListener(EventId.MOUSEOVER)) {
            /*
             * Add listener lazily to avoid extra event if layer is modified in
             * server side listener. This can be removed if "clear and rebuild"
             * style component updates are changed into something more
             * intelligent at some point.
             */
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    marker.addMouseOverListener(new MouseOverListener() {
                        @Override
                        public void onMouseOver(MouseEvent event) {
                            mouseOverRpc.onMouseOver(U.toPoint(event.getLatLng()));
                        }
                    });
                }
            });
        }
        if (hasEventListener(EventId.MOUSEOUT)) {
            marker.addMouseOutListener(new MouseOutListener() {
                @Override
                public void onMouseOut(MouseEvent event) {
                    mouseOutRpc.onMouseOut(U.toPoint(event.getLatLng()));
                }
            });
        }
        if (hasEventListener(EventId.CONTEXTMENU)) {
            marker.addContextMenuListener(new ContextMenuListener() {
                @Override
                public void onContextMenu(MouseEvent event) {
                    contextMenuRpc.onContextMenu(U.toPoint(event.getLatLng()),
                            MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getLeafletMapConnector().getWidget().getElement()));
                }
            });
        }
        String tooltip = getState().tooltip;
        if (tooltip != null) {
            TooltipOptions tooltipOptions = LeafletTooltipConnector.tooltipOptionsFor(getState().tooltipState, this);
            marker.bindTooltip(tooltip, tooltipOptions);
        }
        String popup = getState().popup;
        if (popup != null) {
            PopupOptions popupOptions = LeafletPopupConnector.popupOptionsFor(getState().popupState, this);
            marker.bindPopup(popup, popupOptions);
        }
        addToParent(marker);

        marker.addClickListener(handler);
    }

    private void configureIconSize(DivIconOptions divIconOptions) {
        if (getState().popupAnchor != null) {
            divIconOptions.setPopupAnchor(Point.create(
                    getState().popupAnchor.getLat(),
                    getState().popupAnchor.getLon()));
        }
        if (getState().iconAnchor != null) {
            divIconOptions.setIconAnchor(Point.create(
                    getState().iconAnchor.getLat(),
                    getState().iconAnchor.getLon()));
        }
        if (getState().iconSize != null) {
            divIconOptions.setIconSize(Point.create(
                    getState().iconSize.getLat(),
                    getState().iconSize.getLon()));
        }
    }

    @Override
    protected MarkerOptions createOptions() {
        MarkerOptions markerOptions = MarkerOptions.create();
        return markerOptions;
    }

    @Override
    public Layer getLayer() {
        return marker;
    }

}
