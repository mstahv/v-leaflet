/*
 * Copyright 2012 Vaadin Community.
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
package org.vaadin.addon.leaflet.shared;


import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

/**
 *
 * @author mstahv
 */
public interface LeafletMapServerRpc extends ServerRpc {

	void onClick(Point location, MouseEventDetails details);

	@Delayed(lastOnly=true)
	void onMoveEnd(Bounds bounds, Point center, double zoomlevel);

    void onContextMenu(Point location, MouseEventDetails details);

	void onBaseLayerChange(String name);

	void onOverlayAdd(String name);

	void onOverlayRemove(String name);

    @Delayed(lastOnly = true)
	void onLocate(Point location, Double accuracy, Double altitude, Double speed);

	void onLocateError(String error, int code);

    void onTranslate(Point point);

    void onSize(double x, double y);

}
