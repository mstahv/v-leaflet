package org.vaadin.addonhelpers;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * This abstract class can be used if one e.g. cannot afford TestBench license.
 *
 */
public class AbstractWebDriverCase extends AbstractBaseWebDriverCase {

    protected static final int TEST_PORT = 5678;
    protected static final String BASE_URL = "http://localhost:" + TEST_PORT
            + "/";
    public AbstractWebDriverCase() {
        super();
    }

    @BeforeClass
    public static void startServer() {
        try {
            server = new TServer().startServer(TEST_PORT);
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