package io.xdd.blackscience.socksserver.context;


import io.xdd.blackscience.socksserver.proxy.SocksServer;

/**
 * web context to Manager SocksServer
 * */
public class ContextLoader{

    private SocksServer socksServer;

    public void initSocksServer() {
        if (socksServer==null){
            socksServer=new SocksServer();
            socksServer.start();
        }
    }

    public void closeSocksServer(){
        if (socksServer!=null){
            socksServer.stop();
        }
    }
}
