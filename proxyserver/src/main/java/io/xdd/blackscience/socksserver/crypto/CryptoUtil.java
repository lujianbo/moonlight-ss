package io.xdd.blackscience.socksserver.crypto;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class CryptoUtil {

    /**
     * 返回随机IV
     * */
    public static byte[] generatorIvParameter(int length){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * MD5 迭代 password 直到 长度满足 key_len
     * */
    public static byte[] EVP_BytesToKey(String password,int length){
        byte[] passwordBytes=password.getBytes();
        byte[] temp = new byte[passwordBytes.length + 16];
        byte[] result=new byte[length];//32byte 的key
        int i=0;
        byte[] md5_sum=null;
        while(i<result.length){
            if (i==0){
                md5_sum= DigestUtils.md5(passwordBytes);
            }else{
                //把md5_sum 拷贝到result中
                System.arraycopy(md5_sum,0,temp,0,md5_sum.length);
                System.arraycopy(passwordBytes,0,temp,md5_sum.length,passwordBytes.length);
                md5_sum=DigestUtils.md5(temp);
            }
            System.arraycopy(md5_sum,0,result,i,md5_sum.length);
            i+=md5_sum.length;
        }
        return result;
    }
}
