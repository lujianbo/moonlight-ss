package io.xdd.blackscience.socksserver.local.http.pac;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PacMatcher {

    public static final String regexpRegExp="/^(@@)?\\/.*\\/(?:\\$~?[\\w\\-]+(?:=[^,\\s]+)?(?:,~?[\\w\\-]+(?:=[^,\\s]+)?)*)?$/";

    public static final String keyWordReg="/[^a-z0-9%*][a-z0-9%]{3,}(?=[^a-z0-9%*])/g";

    private Pattern keyWordRegPattern=Pattern.compile(keyWordReg);

    private Pattern regexpRegExpPattern=Pattern.compile(regexpRegExp);

    /**
     * 关键字和 filter 之间的映射
     * */
    private Map<String,List<PacFilter>> filterByKeyword;

    /**
     *
     * */


    /**
     * 根据过滤规则找出其中的关键字
     * */
    public String findKeyword(PacFilter filter){

        String result="";
        String text=filter.getText();
        if (regexpRegExpPattern.matcher(filter.getText()).matches()){
            return result;
        }

        /**
         * 过滤 @@
         * */
        if (text.substring(0, 2).equals("@@"))
        {
            text = text.substring(2);
        }

        String[] candidates= keyWordRegPattern.split(text.toLowerCase());
        if (candidates==null){
            return result;
        }

        Map<String,List<PacFilter>> hash = this.filterByKeyword;
        int resultCount = 16777215;
        int resultLength = 0;
        for (int i = 0, l = candidates.length; i < l; i++)
        {
            String candidate = candidates[i].substring(1);
            int  count = hash.containsKey(candidate) ? hash.get(candidate).size() : 0;
            if (count < resultCount || count == resultCount && candidate.length() > resultLength)
            {
                result = candidate;
                resultCount = count;
                resultLength = candidate.length();
            }
        }
        return result;
    }

}
