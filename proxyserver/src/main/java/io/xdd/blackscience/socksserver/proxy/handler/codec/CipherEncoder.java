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

    private ShadowSocksRequest request;

    public CipherEncoder(Cipher encryptCipher,ShadowSocksRequest request){
        this.encryptCipher = encryptCipher;
        this.request=request;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out=ctx.alloc().buffer();
        ByteBuf in=ctx.alloc().buffer();
        if (!iv_sent){
            iv_sent=true;
            out.writeBytes(encryptCipher.getIV());
            request.encodeAsByteBuf(in);
        }
        in.writeBytes((ByteBuf) msg);
        ByteBuf temp=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher,in,temp);//update 解密
        out.writeBytes(temp);
        ctx.write(out);
    }

}
