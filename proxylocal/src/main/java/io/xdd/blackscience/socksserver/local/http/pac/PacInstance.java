package io.xdd.blackscience.socksserver.local.http.pac;


import java.util.Map;

/**
 * 一个pac 实例,通过导入规则来进行匹配
 * */
public class PacInstance {

    private PacMatcher blacklist;

    private PacMatcher whitelist;

    /**
     * 过滤结果缓存
     * */
    private Map<String,PacFilter> resultCache;


    /**
     * Pac入口
     * */
    public static boolean FindProxyForURL(String url, String host) {
       return true;
    }


    /**
     * 查找 location 匹配哪一条规则
     * */
    public PacFilter matchesAny(String location){
        return null;
    }


}
