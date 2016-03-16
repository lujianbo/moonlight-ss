package io.xdd.blackscience.socksserver.local.common.pac;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jianbo on 2016/3/16.
 */
public  class ProxyAccessController {

    private Map<String,Boolean> cache=new ConcurrentHashMap<>();

    private MapTreeNode node=new MapTreeNode();

    public boolean isProxyHost(String host) {
        if (cache.get(host)==null){
            cache.put(host,node.exit(host));
        }
        return cache.get(host);
    }

    public void addProxy(String host){
        node.add(host);
    }

    public void rebuild(){
        this.cache.clear();
    }
}
