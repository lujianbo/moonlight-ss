package io.xdd.blackscience.socksserver.local.server.context;


import io.xdd.blackscience.socksserver.local.server.ProxyServer;
import io.xdd.blackscience.socksserver.local.server.shadowsocks.BackendServerInitializer;

/**
 * web context to Manager ProxyServer
 * */
public class ContextLoader {

    private ProxyServer proxyServer;

    public void initSocksServer() {
        if (proxyServer==null){
            proxyServer=new ProxyServer(new BackendServerInitializer());
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
