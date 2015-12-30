package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;

import javax.crypto.Cipher;

/**
 * 对流过的数据都进行cipher 的 update操作
 * */
public class CipherEncoder extends ChannelOutboundHandlerAdapter {

    private transient final Cipher encryptCipher;

    public CipherEncoder(Cipher encryptCipher){
        this.encryptCipher = encryptCipher;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf in= (ByteBuf) msg;
        ByteBuf out=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher,in,out);//update 解密
        ctx.write(out,promise);
    }
}
