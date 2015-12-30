package io.xdd.blackscience.socksserver.crypto;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class AESCryptoTest {

    private AESCrypto aesCrypto;

    @Before
    public void before(){
        try {
            aesCrypto=new AESCrypto("mima",32,16);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        Cipher en=aesCrypto.getEncryptCipher();
        Cipher de=aesCrypto.getDecryptCipher();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        for (int i=0;i<10;i++){
            byteArrayOutputStream.write(en.update("x".getBytes()));
        }
        byte[] result=de.update(byteArrayOutputStream.toByteArray());
        System.out.println(new String(result));
    }

    @Test
    public void testKey(){
        try {
            String x="xxx";
            byte[] key=AESCrypto.EVP_BytesToKey(x,32);
            System.out.println(key.length+ "  "+new String(key));
            AESCrypto.generatorKey("xxx",32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetEncryptCipher() throws Exception {

    }
}