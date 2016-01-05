package io.xdd.blackscience.socksserver.common.crypto;

import org.junit.Before;
import org.junit.Test;

public class SSCipherTest {

    SSCipher ssCipher;

    @Test
    public void testUpdate() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        ssCipher=new SSCipher("aes-256-cfb","password",true);
        byte[] result=ssCipher.update("x".getBytes());
        System.out.println(result);
    }


}