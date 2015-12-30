package io.xdd.blackscience.socksserver.context;


import io.xdd.blackscience.socksserver.proxy.ProxyServer;
import io.xdd.blackscience.socksserver.proxy.frontend.FontendServerInitializer;

/**
 * web context to Manager ProxyServer
 * */
public class ContextLoader{

    private ProxyServer proxyServer;

    public void initSocksServer() {
        if (proxyServer==null){
            proxyServer=new ProxyServer(new FontendServerInitializer());
            proxyServer.start();
        }
    }

    public void closeSocksServer(){
        if (proxyServer !=null){
            proxyServer.stop();
        }
    }

    public static void main(String[] args) {
        new ContextLoader().initSocksServer();
    }
}
