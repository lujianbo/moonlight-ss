package com.lujianbo.app.shadowsocks.common.utils;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCipher;
import io.netty.buffer.ByteBuf;

public class BytebufCipherUtil {

    /**
     * 把 in 中的数据update到 out中
     */
    public static void update(ShadowSocksCipher cipher, ByteBuf in, ByteBuf out) {
        int bytes = in.readableBytes();//需要解密的数据大小
        byte[] decryptBuffer = new byte[bytes];
        in.readBytes(decryptBuffer, 0, bytes);
        byte[] result = cipher.update(decryptBuffer);
        out.writeBytes(result);
    }
}
