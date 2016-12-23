package org.vaadin.addonhelpers;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * This abstract class can be used if one e.g. cannot afford TestBench license.
 *
 */
public class AbstractWebDriverCase extends AbstractBaseWebDriverCase {

    protected static final int TESTPORT = 5678;
    protected static final String BASEURL = "http://localhost:" + TESTPORT
            + "/";
    public AbstractWebDriverCase() {
        super();
    }

    @BeforeClass
    public static void startServer() {
        try {
            server = new TServer().startServer(TESTPORT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}