package io.xdd.blackscience.socksserver.local.common.pac;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jianbo on 2016/3/16.
 */
public class ProxyAccessControllerTest {

    @Test
    public void testIsProxyHost() throws Exception {
        ProxyAccessController controller=new ProxyAccessController();
        controller.addProxy("www.baidu.com");
        controller.addProxy("google.com");

        assertFalse(controller.isProxyHost("baidu.com"));
        assertFalse(controller.isProxyHost("abc.baidu.com"));
        assertFalse(controller.isProxyHost("abc.com"));

        assertTrue(controller.isProxyHost("www.baidu.com"));
        assertTrue(controller.isProxyHost("google.com"));
        assertTrue(controller.isProxyHost("www.google.com"));
    }

    @Test
    public void testAddProxy() throws Exception {

    }
}