package org.vaadin.addon.leaflet.demoandtestapp;

import java.util.Arrays;
import java.util.List;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.ui.Notification.Type;
import org.vaadin.addonhelpers.AbstractTest;

public class ExternalLayerControlTest extends AbstractTest {

    private static String attrUSGS = "Tiles courtesy of U.S. Geological Survey";

    private LMap map;
    private LTileLayer currentBaseMap;

    @Override
    public String getDescription() {
        return "Control layers without Leaflet layers control";
    }

    @Override
    public Component getTestComponent() {

        map = new LMap();
        map.setCenter(40, -105.2);
        map.setZoomLevel(11);

        return map;
    }


    @Override
    protected void setup() {
        super.setup();

        Label l = new Label("Without zIndex values, z-order is fouled up as layers are added and removed from map using setActive. " +
                "E.g., changing basemap causes overlays to disappear.<br/>" +
                "Leaflet's Control.Layers.js adds zIndex values to layers, so it does not exhibit this problem.",
                ContentMode.HTML);
        l.setWidth("500px");

        HorizontalLayout hl = new HorizontalLayout();

        hl.addComponents(l, setupBaseMaps(), setupOverlays());

        content.addComponentAsFirst(hl);
    }

    private Component setupOverlays() {
        // These are actually basemaps, but we'll dial down the opacity to use as overlays.
        LayerWrapper shl = createOverlay(
                "https://basemap.nationalmap.gov/ArcGIS/rest/services/USGSShadedReliefOnly/MapServer/tile/{z}/{y}/{x}",
                "USGS Shaded Relief "
        );

        LayerWrapper cl = createOverlay(
                "https://basemap.nationalmap.gov/ArcGIS/rest/services/TNM_Contours/MapServer/tile/{z}/{y}/{x}",
                "USGS Contours"
        );


        CheckBox sh = createOverLayCheckBox(shl);
        CheckBox c = createOverLayCheckBox(cl);

        VerticalLayout vl = new VerticalLayout();
        vl.addComponents(sh, c);
        vl.setSizeUndefined();

        Panel p = new Panel();
        p.setCaption("Overlay maps");
        p.setContent(vl);
        p.setSizeUndefined();

        map.addOverlay(shl.getLayer(), shl.getDescription());
        map.addOverlay(cl.getLayer(), cl.getDescription());
        return p;
    }

    private CheckBox createOverLayCheckBox(LayerWrapper lw) {
        CheckBox c = new CheckBox(lw.getDescription(), true);
        c.setData(lw);
        c.addValueChangeListener(ovListener);

        return c;
    }

    private HasValue.ValueChangeListener ovListener = (HasValue.ValueChangeListener<Boolean>) event -> {
        CheckBox cb = (CheckBox) event.getComponent();
        boolean checked = event.getValue();
        LayerWrapper lw = (LayerWrapper) cb.getData();
        lw.getLayer().setActive(checked);
        trayNotify("Layer " + cb.getCaption() + " should now be " + (checked ? "" : "not") + " visible");
    };

    private LayerWrapper createOverlay(String url, String desc) {
        Double opacity = 0.6;

        LTileLayer l = new LTileLayer();
        l.setUrl(url);
        l.setAttributionString(attrUSGS);
        l.setOpacity(opacity);
        l.setActive(true);

        return new LayerWrapper(desc, l);
    }

    private Component setupBaseMaps() {

        List<LayerWrapper> baseLayers = getBaseLayers();
        currentBaseMap = baseLayers.get(0).getLayer();

        RadioButtonGroup<LayerWrapper> base = new RadioButtonGroup<>("Base maps", baseLayers);
        base.setValue(baseLayers.get(0));


        base.addValueChangeListener(event -> {
            currentBaseMap.setActive(false);
            LayerWrapper lw = event.getValue();
            lw.getLayer().setActive(true);
            currentBaseMap = lw.getLayer();
            trayNotify("Switched base map to " + lw.getDescription());
        });

        for (LayerWrapper lw : baseLayers) {
            map.addBaseLayer(lw.getLayer(), lw.getDescription());
        }

        return base;
    }

    private List<LayerWrapper> getBaseLayers() {

        LTileLayer aer = new LTileLayer();
        aer.setUrl("https://basemap.nationalmap.gov/ArcGIS/rest/services/USGSImageryOnly/MapServer/tile/{z}/{y}/{x}");
        aer.setAttributionString(attrUSGS);
        aer.setActive(false);

        LTileLayer tf = new LTileLayer();
        tf.setUrl("https://{s}.tile.thunderforest.com/transport/{z}/{x}/{y}.png");
        tf.setAttributionString("Tiles Courtesy of <a href=\"https://www.thunderforest.com/\" target=\"_blank\">Thunderforest</a>" +
                "&nbspand OpenStreetMap contributors");
        tf.setSubDomains("a", "b", "c");
        tf.setActive(true);

        return Arrays.asList(new LayerWrapper("ThunderForest Transport ", tf), new LayerWrapper("USGS Aerial", aer));
    }

    private void trayNotify(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    private static class LayerWrapper {
        private String description;
        private LTileLayer layer;

        LayerWrapper(String description, LTileLayer layer) {
            super();
            this.description = description;
            this.layer = layer;
        }

        String getDescription() {
            return description;
        }

        LTileLayer getLayer() {
            return layer;
        }

        @Override
        public String toString() {
            return getDescription();
        }
    }
}
