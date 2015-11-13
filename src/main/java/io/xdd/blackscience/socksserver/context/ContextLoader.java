package io.xdd.blackscience.socksserver.context;


import io.xdd.blackscience.socksserver.core.SocksServer;

import java.util.Properties;

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
