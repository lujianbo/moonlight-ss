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

    private boolean iv_sent=false;

    public CipherEncoder(Cipher encryptCipher){
        this.encryptCipher = encryptCipher;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out=ctx.alloc().buffer();
        if (!iv_sent){
            iv_sent=true;
            out.writeBytes(encryptCipher.getIV());
        }
        ByteBuf in= (ByteBuf) msg;
        ByteBuf temp=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher,in,temp);//update 解密
        out.writeBytes(temp);
        ctx.write(out);
    }
}
