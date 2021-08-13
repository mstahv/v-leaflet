package org.vaadin.addon.leaflet.client;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.ui.ComponentStateUtil;

final class StyleUtil {
    private StyleUtil(){}

    /**
     * Retrieves the primaryStyleName and other styles that are set in the componentState and composes them to
     * one string where each className is separated by an empty space " ".
     * Checks whether values are set or not.
     */
    static String getStyleNameFromComponentState(AbstractComponentState componentState) {
        StringBuilder styleNameBuilder = new StringBuilder();
        if (componentState.primaryStyleName != null)
            styleNameBuilder.append(componentState.primaryStyleName);

        if(ComponentStateUtil.hasStyles(componentState)) {
            for (String s : componentState.styles) {
                if (styleNameBuilder.length() > 0)
                    styleNameBuilder.append(" ");

                styleNameBuilder.append(s);
            }
        }
        return styleNameBuilder.toString();
    }
}
