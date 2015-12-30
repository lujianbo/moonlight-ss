package io.xdd.blackscience.socksserver.proxy.middle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.xdd.blackscience.socksserver.proxy.handler.codec.*;

import javax.crypto.Cipher;

public class ShadowSocksServerConnectHandler extends ChannelOutboundHandlerAdapter {

    private Cipher encryptCipher;

    private Cipher decryptCipher;

    public ShadowSocksServerConnectHandler(String password, ShadowSocksAddressType type, String host, int  port){

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //输出 iv


        //然后
        ctx.pipeline().remove(this);
        //加密和解密
        ctx.pipeline().addLast(new CipherDecoder(null));
        ctx.pipeline().addLast(new CipherEncoder(null));

        //写入 request
        ctx.pipeline().addLast(new ShadowSocksRequestEncoder(null));
    }
}
