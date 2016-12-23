package org.vaadin.addonhelpers;

import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.File;

public class TServer {

    private String webAppPath = "target/testwebapp";

    final AbstractUIProviderImpl uiprovider = new AbstractUIProviderImpl();

    public TServer(String webAppPath) {
        super();
        this.webAppPath = webAppPath;
    }

    public TServer() {
        super();
    }

    public Server startServer() throws Exception {
        return startServer(getPort());
    }

    public Server startServer(int port) throws Exception {

        Server server = new Server();

        final ServerConnector connector = new ServerConnector(server);

        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        WebAppContext context = new WebAppContext();
        VaadinServlet vaadinServlet = createServlet();

        ServletHolder servletHolder = new ServletHolder(vaadinServlet);
        Widgetset annotation = loadWidgetsetAnnotation();
        if (annotation != null) {
            servletHolder.setInitParameter("widgetset", annotation.value());
        }

        File file = new File(webAppPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        context.setWar(file.getPath());
        context.setContextPath("/");

        context.addServlet(servletHolder, "/*");
        configure(context, server);
        server.setHandler(context);
        server.start();
        return server;
    }

    protected VaadinServlet createServlet() {
        VaadinServlet vaadinServlet = new VaadinTestServlet();
        return vaadinServlet;
    }

    protected Widgetset loadWidgetsetAnnotation() {
        Widgetset widgetset =  this.getClass().getAnnotation(Widgetset.class);
        if(widgetset == null) {
            try {
                Class<?> configClass = this.getClass().getClassLoader().loadClass("org.vaadin.addonhelpers.Config");
                widgetset = configClass.getAnnotation(Widgetset.class);
            } catch (ClassNotFoundException e) {
                // Thats ok, configClass might not be present
            }
        }
        return widgetset;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    protected int getPort() {
        return 9998;
    }

    /**
     * Hook for additional configuration.
     *
     * @param context the context
     * @param server the server
     */
    protected void configure(WebAppContext context, Server server) {

    }

    /**
     * Hook to add additional configuration for VaadinServletService
     * 
     * @param service the VaadinServletService
     */
    protected void configureVaadinService(
            final VaadinServletService service) {
        service.addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event)
                    throws ServiceException {
                event.getSession().addUIProvider(uiprovider);
            }
        });
    }

    private class VaadinTestServlet extends VaadinServlet {

        public VaadinTestServlet() {
        }

        @Override
        public void init(ServletConfig servletConfig)
                throws ServletException {
            super.init(servletConfig);
            final VaadinServletService service = getService();
            configureVaadinService(service);
        }
    }

}
