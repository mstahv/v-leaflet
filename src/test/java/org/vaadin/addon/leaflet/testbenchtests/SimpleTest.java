package org.vaadin.addon.leaflet.testbenchtests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.addonhelpers.AbstractWebDriverCase;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class SimpleTest extends AbstractWebDriverCase {

    public static final String TEST_TARGET_PATH = "target/test-classes/";

    @FunctionalInterface
    private interface BreakableFunction<T, R> {

        R applyWithException(T t) throws Throwable;

        public static <T, R> Stream<R> skipBroken(BreakableFunction<T, R> function, T value) {
            try {
                return Stream.of(function.applyWithException(value));
            } catch (Throwable throwable) {
                return Stream.empty();
            }
        }

        ;
    }

    @Test
    public void checkAllTestsOpenWithoutErrors() throws IOException, AssertionError {
        startBrowser();

        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        long testsPassed = getAllTestClasses()
                .map(clazz -> {
                    driver.get(BASE_URL + clazz.getName() + "?debug");
                    waitForLoading();
                    try {
                        WebElement error = driver.findElement(By.className("v-Notification-error"));
                        Assert.fail("Test " + clazz.getName() + " has client side exception");
                    } catch (NoSuchElementException ignored) {
                    }
                    return clazz;
                })
                .count();
        assertNotEquals("Tests passed", 0, testsPassed);
    }

    @SuppressWarnings("unchecked")
    public static Stream<Class<? extends AbstractTest>> getAllTestClasses() {
        Path basePath = Paths.get(TEST_TARGET_PATH + "org/vaadin/addon/leaflet");
        try {
            return (Stream) Files.find(basePath, 100, (path, attrs) -> attrs.isRegularFile())
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".class"))
                    .filter(name -> !name.contains("$"))
                    .map(name -> name.substring(TEST_TARGET_PATH.length()))
                    .map(name -> name.replaceAll("\\.class$", ""))
                    .map(name -> name.replace(File.separatorChar, '.'))
                    .flatMap(name -> BreakableFunction.skipBroken(Class::forName, name))
                    .filter(AbstractTest.class::isAssignableFrom);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void checkServerSideErrors() {
        AbstractTest.allErrors.entrySet().stream().flatMap(entry ->
                {
                    System.err.printf("********************************\nFor class: %s\n",
                            entry.getKey().getName());
                    return entry.getValue().stream();
                }
        ).forEach(errorEvent -> errorEvent.getThrowable().printStackTrace());
        assertTrue(AbstractTest.allErrors.isEmpty());
    }
}
