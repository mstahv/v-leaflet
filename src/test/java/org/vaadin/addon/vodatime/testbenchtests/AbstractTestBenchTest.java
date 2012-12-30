package org.vaadin.addon.vodatime.testbenchtests;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vaadin.addon.vodatime.demoandtestapp.TServer;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.commands.TestBenchCommands;

public class AbstractTestBenchTest {

    protected static final int TESTPORT = 5678;
    protected static final String BASEURL = "http://localhost:" + TESTPORT
            + "/";
    private static final File REF_IMAGE_ROOT = new File(
            "src/test/resources/screenshots");
    protected WebDriver driver;
    protected TestBenchCommands testBench;
    private Server server;

    public AbstractTestBenchTest() {
        super();
    }

    @Before
    public void setUp() {
        Parameters.setScreenshotReferenceDirectory("src/test/resources/screenshots");
        Parameters.setScreenshotErrorDirectory("target");
        Parameters.setCaptureScreenshotOnFailure(true);
        try {
            server = TServer.startServer(TESTPORT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void tearDown() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }

    protected void startBrowser() {
        driver = TestBench.createDriver(new FirefoxDriver());
        // dimension includes browser chrome
        driver.manage().window().setSize(new Dimension(800, 600));
        testBench = (TestBenchCommands) driver;
    }

    public File getReferenceImage(String name) {
        return new File(REF_IMAGE_ROOT, name);
    }

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}