package com.dev.backend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class ServletMain {

	public static void main(String[] args) throws Exception {
		Server _server = new Server(8080);
		
		WebAppContext context = new WebAppContext();
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/mono");
        context.setParentLoaderPriority(true);
        _server.setHandler(context);

        _server.start();
        _server.join();
	}
}
