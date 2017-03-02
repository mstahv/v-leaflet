package org.vaadin.addon.leaflet.demoandtestapp;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addonhelpers.AbstractTest;

public class ViewChangeTest extends AbstractTest {

    @Override
    public String getDescription() {
        return "View change test case";
    }

    private Navigator nav;

    private class MapView extends VerticalLayout implements View {

        private LMap lMap;

        public MapView() {
            this.setSizeFull();
            this.lMap = new LMap();

            this.addComponent(lMap);
            lMap.setSizeFull();

            LTileLayer pk = new LTileLayer();
            pk.setUrl("https://{s}.kartat.kapsi.fi/peruskartta/{z}/{x}/{y}.png");
            pk.setAttributionString("Maanmittauslaitos, hosted by kartat.kapsi.fi");
            pk.setMaxZoom(18);
            pk.setSubDomains("tile2");
            pk.setDetectRetina(true);
            lMap.addBaseLayer(pk, "Peruskartta");

            lMap.setCenter(60.4525, 22.301);
            lMap.setZoomLevel(15);
            lMap.addComponent(new LMarker(60.4525, 22.301));
        }

        @Override
        public void enter(ViewChangeEvent event) {

        }
    }
   
    private class OtherView extends VerticalLayout implements View {
        
        public OtherView() {
            addComponent(new Label("Otherview"));
        }

        @Override
        public void enter(ViewChangeEvent event) {
            
        }
        
    }

    private class TestViewProvider implements ViewProvider {

        @Override
        public String getViewName(String viewAndParameters) {
            return viewAndParameters;
        }

        @Override
        public View getView(String viewName) {

            if (viewName.equals("A")) {
                return new MapView();
            } else if (viewName.equals("B")) {
                return new MapView();
            }
            return new OtherView();

        }

    }

    @Override
    public Component getTestComponent() {
        return null;
    }

    @Override
	protected void setup() {
        content.setSizeFull();

	    nav = new Navigator(this, content);
	    nav.addProvider(new TestViewProvider());
	    nav.navigateTo("A");
	    nav.navigateTo("B");


	}
}
