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

    private static String method="AES/CFB8/NoPadding";

    public static Cipher getEncryptCipher(byte[] key,byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey secretKey=new SecretKeySpec(key,"AES");
        Cipher encryptCipher=Cipher.getInstance(method);
        encryptCipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(iv));
        return encryptCipher;
    }

    public static Cipher getDecryptCipher(byte[] key,byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey secretKey=new SecretKeySpec(key,"AES");
        Cipher decryptCipher=Cipher.getInstance(method);
        decryptCipher.init(Cipher.DECRYPT_MODE,secretKey,new IvParameterSpec(iv));
        return decryptCipher;
    }
}
