package io.xdd.blackscience.socksserver.crypto;

import org.apache.commons.codec.digest.DigestUtils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 返回加密的编码
 * */
public class AESCrypto {

    private transient final Cipher encryptCipher;

    private transient final Cipher decryptCipher;

    //默认的 Lv length 是16byte


    public AESCrypto() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        String password="password";
        SecretKey secretKey=generatorKey("password");
        IvParameterSpec ivParameterSpec=generatorIvParameter();

        encryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);

        decryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
        decryptCipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);

    }

    public Cipher getDecryptCipher() {
        return decryptCipher;
    }

    public Cipher getEncryptCipher() {
        return encryptCipher;
    }


    private static IvParameterSpec generatorIvParameter(){
        IvParameterSpec ivParameterSpec=null;
        return ivParameterSpec;
    }

    /**
     * 将password 生成到 256bit的key
     * */
    private static SecretKey generatorKey(String password) throws NoSuchAlgorithmException {
        byte[] key=EVP_BytesToKey(password);
        return new SecretKeySpec(key,"AES");
    }

    /**
     * MD5 迭代 password直到 长度满足 key_len
     * */
    private static byte[] EVP_BytesToKey(String password){
        byte[] passwordBytes=password.getBytes();
        byte[] temp = new byte[passwordBytes.length + 16];
        byte[] result=new byte[32];
        int i=0;
        byte[] md5_sum=null;
        while(i<32){
            if (i==0){
                md5_sum=DigestUtils.md5(passwordBytes);
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
