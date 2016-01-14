package io.xdd.blackscience.socksserver.proxy.pac;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PacMatcher {

    /**
     *
     * */
    String regexpRegExp="/^(@@)?\\/.*\\/(?:\\$~?[\\w\\-]+(?:=[^,\\s]+)?(?:,~?[\\w\\-]+(?:=[^,\\s]+)?)*)?$/";

    //String optionRegExp="/\\$(~?[\\w\\-]+(?:=[^,\\s]+)?(?:,~?[\\w\\-]+(?:=[^,\\s]+)?)*)$/";

    Pattern regexpRegExpPattern=Pattern.compile(regexpRegExp);

    Map<String,String> filterByKeyword;

    /**
     * 根据规则构建过滤器
     * */
    public void buildFilter(String text){
        if (text.charAt(0)=='!'){
            CommentFilter(text);
        }else{
            RegExpFilter(text);
        }
    }

    /**
     * 该规则已经被注释
     * */
    public void CommentFilter(String text){

    }

    /**
     * 根据规则生成过滤器
     * */
    public void RegExpFilter(String text){
        boolean blocking = true;
        String origText = text;
        if (text.indexOf("@@") == 0){
            blocking = false;
            text = text.substring(2);
        }
    }



    /**
     * 添加规则
     * */
    public void add(String text){
        String keyword=findKeyword(text);//查找关键字
        //替换关键字规则
    }

    //生成关键字
    public String findKeyword(String text){
        String result="";
        Matcher candidates=Pattern.compile("/[^a-z0-9%*][a-z0-9%]{3,}(?=[^a-z0-9%*])/g").matcher(text.toLowerCase());
        Map<String,String> hash = this.filterByKeyword;
        int resultCount = 0xFFFFFF;
        int resultLength = 0;
        for (int i = 0, l = candidates.groupCount(); i < l; i++)
        {
            String candidate = candidates.group(i).substring(1);
            int count = (hash.containsKey(candidate) ? hash.get(candidate).length() : 0);
            if (count < resultCount || (count == resultCount && candidate.length() > resultLength))
            {
                result = candidate;
                resultCount = count;
                resultLength = candidate.length();
            }
        }
        return result;
    }

    Map<String,String> resultCache;
    public String matchesAny(String url,String docDomain)
    {
        String key = url + " " + docDomain + " ";
        String result="";
        if (resultCache.containsKey(key))
        {
            return this.resultCache.get(key);
        }
//        String result = this.matchesAnyInternal(url, 0, docDomain, null, null);
//        if (this.cacheEntries >= CombinedMatcher.maxCacheEntries)
//        {
//            this.resultCache = createDict();
//            this.cacheEntries = 0;
//        }
//        result=this.resultCache.get(key);
//        this.cacheEntries++;
        return result;
    }
}
