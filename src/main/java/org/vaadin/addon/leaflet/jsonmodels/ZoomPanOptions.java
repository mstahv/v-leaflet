/*
 * Copyright 2018 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addon.leaflet.jsonmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import static org.vaadin.addon.leaflet.jsonmodels.VectorStyle.mapper;

/**
 *
 * @author mstahv
 */
public class ZoomPanOptions {
    private Boolean animate;
    private Double duration;
    private Double easeLinearity;
    private Boolean noMoveStart;

    public Boolean getAnimate() {
        return animate;
    }

    public void setAnimate(Boolean animate) {
        this.animate = animate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getEaseLinearity() {
        return easeLinearity;
    }

    public void setEaseLinearity(Double easeLinearity) {
        this.easeLinearity = easeLinearity;
    }

    public Boolean getNoMoveStart() {
        return noMoveStart;
    }

    public void setNoMoveStart(Boolean noMoveStart) {
        this.noMoveStart = noMoveStart;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
