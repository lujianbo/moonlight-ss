package io.xdd.blackscience.socksserver.common.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;

public class CryptoTest {

    @Before
    public void setUp() throws Exception {
        //add Provider
        Security.addProvider(new BouncyCastleProvider());
        Provider[]	providers = Security.getProviders();
        for (int i = 0; i != providers.length; i++)
        {
            System.out.println("Name: " + providers[i].getName() + makeBlankString(15 - providers[i].getName().length())+ " Version: " + providers[i].getVersion());
        }
    }

    @Test
    public void test() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchProviderException, IOException {
//        String method="AES/CFB/NoPadding";
//        byte[] iv= CryptoUtil.generatorIvParameter(16);
//        SecretKey secretKey=new SecretKeySpec(CryptoUtil.EVP_BytesToKey("password",32),"AES");
//        Cipher en=Cipher.getInstance(method,"BC");
//        Cipher de=Cipher.getInstance(method,"BC");
//        en.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(iv));
//        de.init(Cipher.DECRYPT_MODE,secretKey,new IvParameterSpec(iv));
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
    public void testbc(){
//        AESFastEngine engine = new AESFastEngine();
//        CFBBlockCipher cipher = new CFBBlockCipher(engine, 16 * 8);
//        System.out.println(cipher.getAlgorithmName());
//        byte[] xbyte="x".getBytes();
//        byte[] result=new byte[xbyte.length];
//        ParametersWithIV parameterIV = new ParametersWithIV(new KeyParameter(CryptoUtil.EVP_BytesToKey("password",32)),CryptoUtil.generatorIvParameter(16));
//        cipher.init(true,parameterIV);
//        int size=cipher.processBytes(xbyte,0,xbyte.length,result,0);
//        System.out.println(size);
//        System.out.println(result[0]);
//        System.out.println(byteArrayOutputStream.size());
//        byte[] result=de.update(byteArrayOutputStream.toByteArray());
//        System.out.println(new String(result));
    }

    @After
    public void tearDown() throws Exception {

    }


    public static String makeBlankString(
            int	len)
    {
        char[]   buf = new char[len];

        for (int i = 0; i != buf.length; i++)
        {
            buf[i] = ' ';
        }

        return new String(buf);
    }
}