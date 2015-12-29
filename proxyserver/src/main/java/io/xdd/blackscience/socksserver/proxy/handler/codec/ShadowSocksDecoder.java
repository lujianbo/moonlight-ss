package io.xdd.blackscience.socksserver.proxy.handler.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.crypto.Cipher;


public class ShadowSocksDecoder extends ChannelInboundHandlerAdapter {

    private transient final Cipher decryptCipher;

    public ShadowSocksDecoder(Cipher decryptCipher) {
        this.decryptCipher = decryptCipher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in= (ByteBuf) msg;
        ByteBuf decrypt=ctx.alloc().buffer(in.capacity());
        decryptCipher.update(in.nioBuffer(),decrypt.nioBuffer());
        ctx.fireChannelRead(decrypt);
    }
}
