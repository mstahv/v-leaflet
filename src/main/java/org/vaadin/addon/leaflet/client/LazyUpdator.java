
package org.vaadin.addon.leaflet.client;

import com.google.gwt.core.client.Scheduler;
import java.util.HashSet;

/**
 *
 * @author mattitahvonenitmill
 */
public class LazyUpdator {
    
    private static final HashSet<AbstractLeafletLayerConnector> dirtySet = new HashSet<AbstractLeafletLayerConnector>();
    private static boolean scheduled;
    
    public static void defer(AbstractLeafletLayerConnector c) {
        dirtySet.add(c);
        if(!scheduled) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    scheduled = false;
                    for (AbstractLeafletLayerConnector c : dirtySet) {
                        c.updateIfDirty();
                    }
                    dirtySet.clear();
                }
            });
        }
    }
    
    public static void clear(AbstractLeafletLayerConnector c) {
        dirtySet.remove(c);
    }

}
