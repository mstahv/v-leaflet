package org.vaadin.addon.vodatime.testbenchtests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SimpleTest extends AbstractTestBenchTest {

    @Test
    public void basic() throws IOException, AssertionError {
        startBrowser();

        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);

        driver.get(BASEURL + "BasicTest");
        
        testBench.resizeViewPortTo(640, 400);

    }

}
