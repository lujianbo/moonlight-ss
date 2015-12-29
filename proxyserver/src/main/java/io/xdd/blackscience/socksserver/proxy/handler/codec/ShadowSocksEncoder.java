package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;

public class ShadowSocksEncoder extends ChannelOutboundHandlerAdapter {

    private boolean isSent=false;

    private transient final Cipher encryptCipher;

    private ShadowSocksRequest request;

    public ShadowSocksEncoder(ShadowSocksRequest request, Cipher encryptCipher){
        this.encryptCipher = encryptCipher;
        this.request=request;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf temp = ctx.alloc().buffer(4);
        if (!isSent){
            isSent=true;
            //发送iv加密串
            temp.writeBytes(encryptCipher.getIV());
            //发送 request 信息
            request.encodeAsByteBuf(temp);

        }
        temp.writeBytes((ByteBuf) msg);
        ByteBuf realOut=ctx.alloc().buffer(temp.capacity());
        ByteBuffer in=temp.nioBuffer();
        ByteBuffer out=realOut.nioBuffer();
        encryptCipher.update(in,out);
        ctx.write(realOut,promise);
    }
}
