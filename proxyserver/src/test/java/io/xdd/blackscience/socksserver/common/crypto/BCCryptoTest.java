package io.xdd.blackscience.socksserver.common.crypto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BCCryptoTest {

    @Test
    public void match(){

        String str="type[index][opt][value]";

        for (String s:sub(str)){
            System.out.println(s);
        }

    }

    public List<String> sub(String text){
        //检查合法性
        if (!Pattern.compile("^\\w+(\\[\\w+\\])+$").matcher(text).matches()){
            return null;
        }
        //提取参数
        Pattern pattern=Pattern.compile("(\\w+)|(?<=\\[)\\w+(?=\\])");
        Matcher m=pattern.matcher(text);
        List<String> result=new ArrayList<>();
        while (m.find()){
            result.add(m.group());
        }
        return result;
    }

    public void test(){
        //对 形如
        String str="type[index][opt][value]";
        //的格式进行分别处理
        String[] cast={};
    }

}