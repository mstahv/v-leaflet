[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/v-leaflet)
[![Stars on vaadin.com/directory](https://img.shields.io/vaadin-directory/star/v-leaflet.svg)](https://vaadin.com/directory/component/v-leaflet)

# V-Leaflet add-on project

This is a Vaadin add-on that provides server side API for the amazing [LeafletJS](http://leafletjs.com). For usage examples see tests in this project. There is also a "full stack" usage example, but it only uses fraction of feature provided by this project.

Release downloads:
[Vaadin Directory](https://vaadin.com/directory/) 

Examples (from test code):
https://github.com/mstahv/v-leaflet/tree/master/src/test/java/org/vaadin/addon/leaflet/demoandtestapp

V-Leaflet example app: 
https://github.com/mstahv/vleafletexample

The implementation depends on g-leaflet project. During development you'll most likely want to check out that as well. When using the add-on, dependency should be automatically resolved.

https://github.com/mstahv/g-leaflet

## Add-ons for add-ons

This add-on contains just core Leaflet API wrapper. Leaflet has rich set of extensions, that again can have wrappers for Vaadin/GWT. Currently draw features are built in, but they'll most likely by split into separate add-on soon as well. You'll find builds of these via Vaadin Directory as well.

 * [Leaflet.markercluster](https://github.com/willtemperley/v-leaflet-markercluster-parent) - Must have if you have thousands of Markers
 * [Shramov plugins](https://github.com/mstahv/v-leaflet-shramov-parent) - When your client says there is Google Maps in "requirements"
 * [Leaflet.Draw](https://github.com/mstahv/v-leaflet-draw) The most popular drawing/editing extension to Leaflet
 * Leaflet.label (Any volunteers to work this? It would probably be quite popular)


### Instructions for add-on developers

Project is built with Maven so it should be IDE agnostic and you should manage with basic maven skills without instructions. Basic steps during development are discussed below. Most actions can be easily accessed via IDE. Eclipse users should install m2e plugin.

I can already hear some of you asking "Why maven, I hate maven!?". All build systems have their weak parts and maven is not alone here. I hate maven sometimes as well. Most often though maven is hated because developers are lazy and they don't want to understand the core concepts. If can dare you understand concepts of object oriented programming I could suggest you to at least try with Maven concepts. Here are some additional reasons:

 * Transient dependencies and massive library repository(ies)
 * It is not that hard
 * Maven is commonly used. Nowadays (at least among advanced java developers) more common than ant.
 * It drives towards "standars" so something learned on one project can be adapted on other
 * Wide IDE support -> extremely fast to start working on a project for real. If pom.xml properly set up a custom version can be started with "single click". TODO video tutorial

### Check out sources

Sources for this project are located in github. Github has good instructions how to check out the source code to your computer.

https://github.com/mstahv/v-leaflet

### Run full build

To packge and install addon-on jar to local repository you can assign following command:
```
mvn install -DskipTests
```

Optionally you might wish to do "clean build" by calling clean target before:

```
mvn clean install -DskipTests
```

During the install process tests are run automatically. As it requires GWT compiling test widgetset (if not up to date) and running TestBench/Selenium tests it might take a while. Tests are run with PhantomJS, so it must be available on your path. If you wish to bypass tests run following command:

```
mvn install -DskipTests
```

### Build Directory compatible zip package

The Vaadin Directory supports both jar and zip packaging. Zip packages have more features (like support for src and javadoc jar, supports adding e.g. required dependencies for non maven users and adding documentation in the zip package).

The zip package can be quilt using following command (also builds jar):
```
mvn install assembly:single
```

In this build following stuff is collected to the zip file:

 * add-on jar file
 * add-on sources jar file (most often enough for IDE users to get javadocs as well)
 * project dependecies with scope "compile" are collected to libs directory. For those poor souls who don't use a build system that supports transient dependencies this directory will help a lot when they start to use this add-on.
 * This readme file

The zip packagin can be customized by modifying assembly.xml file.

== Running just tests ==

Tests written for this add-on are automatically executed during build but only running tests works with following command:
```
mvn test
```

To demonstrate how properly test you add-on the project has example of a simple unit test and a bit more advanced TestBench (or WebDriver) test to stress the whole chain from browser to server and back. TestBench is highly suggested (especially the soon to be released version 3), but one can so simple stuff with plain Selenium3 as well. The project has TestBench version commented out as its license might not fit for all developers if they have no Vaadin Pro Account subscription.

Unit tests can be written with tool of choice (maven supports most of them). JUnit is already configured.

### Manually running test server and test apps during development

The UiRunner class has a main method that you can launch. It opens an embedded jetty to port 9998. The main view lists all tests and clicking one opens it in a new window. The easiest method is usually running that from you IDE (e.g. open the class and then hit CTRL/CMD-F11 in Eclipse). 

From command line one can launch it with easily with correct classpath with maven exec plugin:
```
mvn -e exec:java@test
```

Tests are loaded via reflection on each page load. Accessing http://localhost:9998/BasicTest will load a new instance of test class org.vaadin.addon.leaflet.demoandtestapp.BasicTest

### Client side development aka GWT development

The client side code is developed with GWT. Just recompiling the widgetset happens with following command:
```
mvn vaadin:compile
```

The development mode (aka hosted mode) is launched with target "vaadin:debug" or "vaadin:run". For the former you will attach a remote debugger. E.g. in e.g. Eclipse that is simple and m2e automatically sets the correct classpath. "Superdevmode" can be launched with target "vaadin:run-codeserver".

