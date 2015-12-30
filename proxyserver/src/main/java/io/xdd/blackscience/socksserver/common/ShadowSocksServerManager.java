package io.xdd.blackscience.socksserver.common;


/**
 * 服务器管理器,负责管理已经注册的服务器
 * */
public class ShadowSocksServerManager {


    /**
     * 返回一个最佳的服务器地址
     * */
    public ShadowSocksServerInstance getOne(){
        return null;
    }

    private static ShadowSocksServerManager ourInstance = new ShadowSocksServerManager();

    public static ShadowSocksServerManager getInstance() {
        return ourInstance;
    }
}
