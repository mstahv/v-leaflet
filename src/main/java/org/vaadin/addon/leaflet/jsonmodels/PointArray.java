package org.vaadin.addon.leaflet.jsonmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.vaadin.addon.leaflet.shared.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PointArray extends ArrayList<Point> {
    public PointArray() {
    }

    public PointArray(Point... points) {
        this(Arrays.asList(points));
    }

    public PointArray(Collection<? extends Point> c) {
        super(c);
    }
    
    public void sanitize() {
        removeAll(Collections.singleton(null));
    }

    public String asJson() {
        try {
            return VectorStyle.mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
