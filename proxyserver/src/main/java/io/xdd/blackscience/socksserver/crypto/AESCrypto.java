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

    public AESCrypto(String password,int keyLength,int ivLength) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        SecretKey secretKey=generatorKey(password,keyLength);
        IvParameterSpec ivParameterSpec=generatorIvParameter(ivLength);

        encryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);

        decryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
        decryptCipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);

    }

    public AESCrypto(String password,int keyLength,byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey secretKey=generatorKey(password,keyLength);
        IvParameterSpec ivParameterSpec=new IvParameterSpec(iv);

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

    /**
     * 返回随机IV
     * */
    private static IvParameterSpec generatorIvParameter(int length){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new IvParameterSpec(bytes);
    }

    /**
     * 将password 生成到 256bit的key
     * */
    private static SecretKey generatorKey(String password,int keyLength) throws NoSuchAlgorithmException {
        byte[] key=EVP_BytesToKey(password,keyLength);
        return new SecretKeySpec(key,"AES");
    }

    /**
     * MD5 迭代 password直到 长度满足 key_len
     * */
    private static byte[] EVP_BytesToKey(String password,int length){
        byte[] passwordBytes=password.getBytes();
        byte[] temp = new byte[passwordBytes.length + 16];
        byte[] result=new byte[length];//32byte 的key
        int i=0;
        byte[] md5_sum=null;
        while(i<result.length){
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
