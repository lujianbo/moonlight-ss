package io.xdd.blackscience.socksserver.local.pac;


import java.util.regex.Pattern;

/**
 * 过滤规则
 * */
public class PacFilter {

    /**
     * 原始的规则
     * */
    private String text;

    private Pattern regexp;

    private String regexpSource;

    /**
     * 根据规则来构建过滤器
     * */
    private PacFilter(String text){
        this.text=text;
    }

    /**
     * 构建正则表达式
     * */
    public void regexp(String regexpSource){
        if (regexpSource.length() >= 2 && regexpSource.charAt(0) == '/' && regexpSource.charAt(regexpSource.length() - 1) == '/')
        {
            this.regexp=Pattern.compile(regexpSource.substring(1, regexpSource.length() - 2),Pattern.CASE_INSENSITIVE);
        }
        else
        {
            this.regexpSource = regexpSource;
        }
    }

    //根据规则生成 正则表达式
    public Pattern getRegexp(){
        String source=this.regexpSource
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

        return Pattern.compile(source,Pattern.CASE_INSENSITIVE);
    }



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
     * 检查 url 能否被 这个过滤
     * */
    public boolean matches(String url){
        return true;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRegexp(Pattern regexp) {
        this.regexp = regexp;
    }

    public String getRegexpSource() {
        return regexpSource;
    }

    public void setRegexpSource(String regexpSource) {
        this.regexpSource = regexpSource;
    }
}
