package org.vaadin.addon.leaflet.demoandtestapp.util;

import java.io.File;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ServerRpcManager.RpcInvocationException;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.ClientConnector.ConnectorErrorEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public class TServer {

	private static final int PORT = 9998;

	/**
	 * 
	 * Test server for the addon.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Server server = startServer(PORT);
	}

	public static Server startServer(int port) throws Exception {

		final AbstractUIProviderImpl uiprovider = new AbstractUIProviderImpl();

		Server server = new Server();

		final Connector connector = new SelectChannelConnector();

		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext();
		VaadinServlet vaadinServlet = new VaadinServlet() {
			@Override
			public void init(ServletConfig servletConfig)
					throws ServletException {
				super.init(servletConfig);
				getService().addSessionInitListener(new SessionInitListener() {
					@Override
					public void sessionInit(SessionInitEvent event)
							throws ServiceException {
						event.getSession().addUIProvider(uiprovider);
						event.getSession().setErrorHandler(new ErrorHandler() {

							@Override
							public void error(ErrorEvent event) {
								Throwable t = event.getThrowable();
								if (t instanceof SocketException) {
									// Most likely client browser closed socket
									Logger.getAnonymousLogger()
											.info("SocketException in CommunicationManager."
													+ " Most likely client (browser) closed socket.");
									return;
								}

								if (event instanceof ConnectorErrorEvent) {

									if (event.getThrowable() instanceof RpcInvocationException) {
										RpcInvocationException ex = (RpcInvocationException) event
												.getThrowable();
										Throwable theActualCauseThatDevelopersAreReallyInterestedOfAndWhichShouldBeDisplaydInUIAndInLogForDeveloperUX = ex
												.getCause().getCause()
												.getCause();
										
										t = theActualCauseThatDevelopersAreReallyInterestedOfAndWhichShouldBeDisplaydInUIAndInLogForDeveloperUX;
									}

									ConnectorErrorEvent e = (ConnectorErrorEvent) event;
									com.vaadin.shared.Connector connector = e
											.getConnector();
									Component c;
									if (connector instanceof Component) {
										c = (Component) connector;
									} else {
										c = ((Component) connector).getParent();
									}

									if (c instanceof AbstractComponent) {
										AbstractComponent component = (AbstractComponent) c;
										if (component != null) {
											// Shows the error in
											// AbstractComponent
											ErrorMessage errorMessage = AbstractErrorMessage
													.getErrorMessageForException(t);
											component
													.setComponentError(errorMessage);
										}
									}
								}

								// also print the error on console
								Logger.getAnonymousLogger().log(Level.SEVERE, "", t);
							}
						});
					}
				});
			}

		};

		ServletHolder servletHolder = new ServletHolder(vaadinServlet);
		servletHolder.setInitParameter("widgetset",
				"org.vaadin.addon.leaflet.demoandtestapp.TestWidgetset");

		File file = new File("target/testwebapp");
		context.setWar(file.getPath());
		context.setContextPath("/");

		context.addServlet(servletHolder, "/*");
		server.setHandler(context);
		server.start();
		return server;
	}
}
