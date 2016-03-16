package io.xdd.blackscience.socksserver.local.common.pac;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *        google.com 将等同于 *.google.com
 *        *.google 意味着全匹配
 *
 */
public class MapTreeNode {

    private ConcurrentHashMap<String,MapTreeNode> map;

    public MapTreeNode(){
        this.map= new ConcurrentHashMap<>();
    }

    /**
     * 判断host是否存在于树中
     * */
    public boolean exit(String host){
        MapTreeNode node=this;
        for (String str:processRule(host)){
            node = node.get(str);
            if (node==null){
                return false;
            }else if (node.get("*")!=null){
                return true;//匹配到 *则立即返回
            }
        }
        return true;
    }

    public void add(String host){
        MapTreeNode node=this;
        for (String key:processRule(host)){
            if (node.get(key)==null){
                node.put(key,new MapTreeNode());
            }
            node=node.get(key);
        }
    }

    private String[] processRule(String str){
        String[] strings=str.split("\\.");
        int length=strings.length;
        String[] newString=new String[length+1];//最后一个留给默认的 *
        for (int i=0;i<length;i++){
            newString[i]=strings[length-i-1];
        }
        newString[length]="*";//添加*
        return newString;
    }

    private MapTreeNode get(String key){
        return map.get(key);
    }

    private void put(String key,MapTreeNode node){
        this.map.put(key,node);
    }

}
