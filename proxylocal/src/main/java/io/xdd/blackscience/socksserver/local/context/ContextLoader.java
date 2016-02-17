package io.xdd.blackscience.socksserver.local.context;


import io.xdd.blackscience.socksserver.local.ProxyServer;
import io.xdd.blackscience.socksserver.local.socksadapter.FrontendServerInitializer;

/**
 * web context to Manager ProxyServer
 * */
public class ContextLoader{

    private ProxyServer proxyServer;

    public void initSocksServer() {
        if (proxyServer==null){
            proxyServer=new ProxyServer(new FrontendServerInitializer());
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
