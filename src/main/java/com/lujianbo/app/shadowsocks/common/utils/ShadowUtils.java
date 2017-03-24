package com.lujianbo.app.shadowsocks.common.utils;

import io.netty.util.internal.StringUtil;

public class ShadowUtils {

    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final int XOR_DEFAULT_VALUE = 0xff;

    public static String intToIp(int i) {
        return String.valueOf(i >> FIRST_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.' +
                (i >> SECOND_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.' +
                (i >> THIRD_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.' +
                (i & XOR_DEFAULT_VALUE);
    }

    private static final char[] ipv6conseqZeroFiller = {':', ':'};
    private static final char ipv6hextetSeparator = ':';

    /**
     * Convert numeric IPv6 to compressed format, where
     * the longest sequence of 0's (with 2 or more 0's) is replaced with "::"
     */
    public static String ipv6toCompressedForm(byte[] src) {
        assert src.length == 16;
        //Find the longest sequence of 0's
        //start of compressed region (hextet index)
        int cmprHextet = -1;
        //length of compressed region
        int cmprSize = 0;
        for (int hextet = 0; hextet < 8; ) {
            int curByte = hextet * 2;
            int size = 0;
            while (curByte < src.length && src[curByte] == 0
                    && src[curByte + 1] == 0) {
                curByte += 2;
                size++;
            }
            if (size > cmprSize) {
                cmprHextet = hextet;
                cmprSize = size;
            }
            hextet = curByte / 2 + 1;
        }
        if (cmprHextet == -1 || cmprSize < 2) {
            //No compression can be applied
            return ipv6toStr(src);
        }
        StringBuilder sb = new StringBuilder(39);
        ipv6toStr(sb, src, 0, cmprHextet);
        sb.append(ipv6conseqZeroFiller);
        ipv6toStr(sb, src, cmprHextet + cmprSize, 8);
        return sb.toString();
    }

    /**
     * Converts numeric IPv6 to standard (non-compressed) format.
     */
    public static String ipv6toStr(byte[] src) {
        assert src.length == 16;
        StringBuilder sb = new StringBuilder(39);
        ipv6toStr(sb, src, 0, 8);
        return sb.toString();
    }

    private static void ipv6toStr(StringBuilder sb, byte[] src, int fromHextet, int toHextet) {
        int i;
        toHextet--;
        for (i = fromHextet; i < toHextet; i++) {
            appendHextet(sb, src, i);
            sb.append(ipv6hextetSeparator);
        }

        appendHextet(sb, src, i);
    }

    private static void appendHextet(StringBuilder sb, byte[] src, int i) {
        StringUtil.toHexString(sb, src, i << 1, 2);
    }


}
