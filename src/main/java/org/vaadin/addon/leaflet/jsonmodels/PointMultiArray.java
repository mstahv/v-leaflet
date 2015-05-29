package org.vaadin.addon.leaflet.jsonmodels;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collection;

public class PointMultiArray extends ArrayList<PointArray> {

    public PointMultiArray() {
    }

    public PointMultiArray(Collection<? extends PointArray> c) {
        super(c);
    }

    public String asJson() {
        try {
            return VectorStyle.mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sanitize() {
        for (PointArray pa : this) {
            pa.sanitize();
        }
    }

}
