package org.vaadin.addon.leaflet.testbenchtests;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import com.vaadin.ui.UI;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.addonhelpers.AbstractWebDriverCase;


public class SimpleTest extends AbstractWebDriverCase {

    public static final String TEST_TARGET_PATH = "target/test-classes/";

    @FunctionalInterface
    private interface BreakableFunction<T,R> {

        R applyWithException (T t) throws Throwable;

        public static <T,R> Stream<R> skipBroken(BreakableFunction<T,R> function, T value){
            try {
                return Stream.of(function.applyWithException(value));
            } catch (Throwable throwable) {
                return Stream.empty();
            }
        };
    }

    @Test
    public void checkAllTestsOpenWithoutErrors() throws IOException, AssertionError {
        startBrowser();

        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        Path basePath = Paths.get(TEST_TARGET_PATH + "org/vaadin/addon/leaflet");
        Files.find(basePath, 100, (path, attrs) -> true)
                .map(Path::toString)
                .filter(name -> name.endsWith(".class"))
                .filter(name -> !name.contains("$"))
                .map(name -> name.substring(TEST_TARGET_PATH.length()))
                .map(name -> name.replaceAll("\\.class$", ""))
                .map(name -> name.replace(File.separatorChar, '.'))
                .flatMap(name->BreakableFunction.skipBroken(Class::forName,name))
                .filter(AbstractTest.class::isAssignableFrom)
                .forEach(clazz -> {
                    driver.get(BASEURL + clazz.getName() + "?debug");
                    waitForLoading();
                    try {
                        WebElement error = driver.findElement(By.className("v-Notification-error"));
                        Assert.fail("Test " + clazz.getName() + " has client side exception");
                    } catch (NoSuchElementException ignored) {
                    }
                });

    }

}
