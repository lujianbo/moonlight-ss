package io.xdd.blackscience.socksserver.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Provider;
import java.security.Security;

import static org.junit.Assert.*;

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
    public void test(){

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