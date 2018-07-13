/*
 * Copyright 2014 Vaadin Community.
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

package org.vaadin.addon.leaflet;

/**
 * A layer that uses OSM tiles.
 * <p>
 * Be sure to check if your usage comforts to their usage policy. For more
 * serious usage, you should most likely buy TMS service e.g. from mapbox.com.
 */
public class LOpenStreetMapLayer extends LTileLayer {

    public LOpenStreetMapLayer() {
        super("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
        setAttributionString("Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors");
    }

}
