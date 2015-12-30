package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;

import javax.crypto.Cipher;
import java.util.List;


/**
 * IV 作为数据开头的加密处理
 * */
public class CipherDecoder extends ChannelInboundHandlerAdapter {

    private transient final Cipher decryptCipher;

    public CipherDecoder(Cipher decryptCipher) {
        this.decryptCipher = decryptCipher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in= (ByteBuf) msg;
        ByteBuf out=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(decryptCipher,in,out);//update 解密
        ctx.fireChannelRead(out);
    }
}
