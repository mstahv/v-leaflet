package org.vaadin.addon.leaflet.testbenchtests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.vaadin.addon.leaflet.demoandtestapp.util.TListUi;

import com.vaadin.data.Container;

public class SimpleTest extends AbstractTestBenchTest {

    @Test
    public void checkAllTestsOpenWithoutErrors() throws IOException, AssertionError {
        startBrowser();

        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        
        Container listTestClasses = TListUi.listTestClasses();
        for (Object id : listTestClasses.getItemIds()) {
			String name = (String) listTestClasses.getItem(id)
					.getItemProperty("name").getValue();
			
			driver.get(BASEURL + name + "?debug");
			try {
				WebElement error = driver.findElement(By.className("v-Notification-error"));
				Assert.fail("Test " + name + " has client side exception");
			} catch (NoSuchElementException e) {
				continue;
			}
			
		}
        
    }

}
