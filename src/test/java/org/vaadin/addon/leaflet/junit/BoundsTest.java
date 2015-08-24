package org.vaadin.addon.leaflet.junit;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.addon.leaflet.shared.Bounds;

/**
 *
 * @author Matti Tahvonen
 */
public class BoundsTest {

    @Test
    public void testNortWest() {

        Bounds bounds = new Bounds();

        final double latBase = 42.3791618;
        final double lonBase = -71.1138139;

        final String[] names = new String[]{"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen", "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson", "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson", "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith", "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin", "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen", "Jaydan Jackson", "Bernard Nilsen"};
        Random r = new Random(0);
        for (String name : names) {
            double lat = latBase + 0.02 * r.nextDouble() - 0.02 * r.
                    nextDouble();
            double lon = lonBase + 0.02 * r.nextDouble() - 0.02 * r.
                    nextDouble();
            bounds.extend(lat, lon);
        }

        Assert.assertTrue(bounds.getNorthEastLat() > latBase);
        Assert.assertTrue(bounds.getNorthEastLon() > lonBase);
        Assert.assertTrue(bounds.getSouthWestLat() < latBase);
        Assert.assertTrue(bounds.getSouthWestLon() < lonBase);

        Assert.assertTrue(bounds.getNorthEastLat() > 42);
        Assert.assertTrue(bounds.getNorthEastLat() < 43);

        Assert.assertTrue(bounds.getSouthWestLat() > 42);
        Assert.assertTrue(bounds.getSouthWestLat() < 43);

        Assert.assertTrue(bounds.getSouthWestLon() < -71);
        Assert.assertTrue(bounds.getSouthWestLon() > -72);

        Assert.assertTrue(bounds.getNorthEastLon()< -71);
        Assert.assertTrue(bounds.getNorthEastLon() > -72);

    }

}
