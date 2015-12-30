package io.xdd.blackscience.socksserver.context;


import io.xdd.blackscience.socksserver.proxy.ProxyServer;

/**
 * web context to Manager ProxyServer
 * */
public class ContextLoader{

    private ProxyServer proxyServer;

    public void initSocksServer() {
//        if (proxyServer==null){
//            proxyServer=new ProxyServer();
//            proxyServer.start();
//        }
    }

    public void closeSocksServer(){
        if (proxyServer !=null){
            proxyServer.stop();
        }
    }
}
