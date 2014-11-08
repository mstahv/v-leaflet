package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONParser;
import com.vaadin.shared.ui.Connect;
import org.peimari.gleaflet.client.Polygon;
import org.peimari.gleaflet.client.PolylineOptions;

@Connect(org.vaadin.addon.leaflet.LPolygon.class)
public class LeafletPolygonConnector extends LeafletPolylineConnector {

    @Override
    protected Polygon createVector(PolylineOptions options) {
        return Polygon.createWithHoles(getCoordinatesTwoDimArray(), options);
    }

    protected JsArray<JsArray<JsArray>> getCoordinatesTwoDimArray() {
        return JSONParser.parseStrict(getState().geometryjson).isArray().getJavaScriptObject().cast();
    }

}
