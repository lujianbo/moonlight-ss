package io.xdd.blackscience.socksserver.local.common.pac;

import static org.junit.Assert.*;

/**
 * Created by jianbo on 2016/3/16.
 */
public class PacMatcherTest {

    @org.junit.Test
    public void testFindKeyword() throws Exception {

        PacMatcher matcher=new PacMatcher();
        PacFilter filter=new PacFilter("||365safego.com^");
        String result=matcher.findKeyword(filter);
        System.out.println(result);

    }
}