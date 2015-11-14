package io.xdd.blackscience.socksserver.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener extends ContextLoader implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.initSocksServer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.closeSocksServer();
    }
}
