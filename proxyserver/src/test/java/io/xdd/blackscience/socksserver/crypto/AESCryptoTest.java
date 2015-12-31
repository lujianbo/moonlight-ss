package io.xdd.blackscience.socksserver.crypto;

import org.apache.commons.codec.binary.Hex;
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

    }

    @Test
    public void test() throws Exception {
//        Cipher en=aesCrypto.getEncryptCipher();
//        Cipher de=aesCrypto.getDecryptCipher();
//        System.out.println(en.getAlgorithm());
//        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//        for (int i=0;i<10;i++){
//            byte[] xbyte="x".getBytes();
//            byteArrayOutputStream.write(en.update(xbyte));
//        }
//        System.out.println(byteArrayOutputStream.size());
//        byte[] result=de.update(byteArrayOutputStream.toByteArray());
//        System.out.println(new String(result));
    }

    @Test
    public void testKey(){
//        try {
//            String x="mima";
//            byte[] key=AESCrypto.EVP_BytesToKey(x,32);
//            System.out.println(key.length+ "  "+ Hex.encodeHexString(key));
//            // 3f572fcb0f9af03848738946954b8c433cc1d1554fba03ea7f619ed98fb010e8
//            AESCrypto.generatorKey(x,32);
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testIV(){
//        String password="password";
//        byte[] iv = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };
//        try {
//            AESCrypto a=new AESCrypto(password,32,iv);
//            byte[] content= new byte[]{0x03,0x0f,0x77,0x77,0x77,0x2e,0x6f,0x73,0x63,0x68,0x69,0x6e,0x61,0x2e,0x6e,0x65,0x74,0x00,0x50};
//            Cipher en=a.getEncryptCipher();
//            byte[] enContent=en.update(content);
//            System.out.println(Hex.encodeHexString(enContent));//3f0bce780d638f
//            Cipher de=a.getDecryptCipher();
//            System.out.println(new String(de.update(enContent)));
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
//            e.printStackTrace();
//        }

    }

    @Test
    public void testGetEncryptCipher() throws Exception {

    }
}