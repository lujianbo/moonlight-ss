package io.xdd.blackscience.socksserver.proxy.pac;


import java.util.regex.Pattern;

/**
 * 过滤规则
 * */
public class PacFilter {

    //整理text
    public String normalize(String text){
        text = text.replace("/[^\\S ]/g", "");

        if (Pattern.compile("/^\\s*!/").matcher(text).matches()){
            // Don't remove spaces inside comments
            return text.trim();
        }else {
            return text.replace("/\\s/g","");
        }
    }

    /**
     *
     * */
    public String toRegExp(String text){
        return text
                .replace("/\\*+/g", "*")        // remove multiple wildcards
                .replace("/\\^\\|$/", "^")       // remove anchors following separator placeholder
                .replace("/\\W/g", "\\$&")      // escape special symbols
                .replace("/\\\\\\*/g", ".*")      // replace wildcards by .*
                // process separator placeholders (all ANSI characters but alphanumeric characters and _%.-)
                .replace("/\\\\\\^/g", "(?:[\\x00-\\x24\\x26-\\x2C\\x2F\\x3A-\\x40\\x5B-\\x5E\\x60\\x7B-\\x7F]|$)")
                .replace("/^\\\\\\|\\\\\\|/", "^[\\w\\-]+:\\/+(?!\\/)(?:[^\\/]+\\.)?") // process extended anchor at expression start
                .replace("/^\\\\\\|/", "^")       // process anchor at expression start
                .replace("/\\\\\\|$/", "$")       // process anchor at expression end
                .replace("/^(\\.\\*)/", "")      // remove leading wildcards
                .replace("/(\\.\\*)$/", "");     // remove trailing wildcards
    }


    /**
     * 检查 url 能否被 这个过滤
     * */
    public boolean matches(String url){
        return true;
    }
}
