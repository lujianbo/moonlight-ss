package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.xdd.blackscience.socksserver.common.crypto.SSCipher;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;


/**
 * 对流过的数据都进行cipher 的 update操作
 * */
public class SSEncoder extends ChannelOutboundHandlerAdapter {

    private transient SSCipher encryptCipher;

    private boolean iv_sent=false;


    public SSEncoder(String method, String password){
        encryptCipher=new SSCipher(method,password,true);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out=ctx.alloc().buffer();
        if (!iv_sent){
            iv_sent=true;
            out.writeBytes(encryptCipher.getIv());
        }
        ByteBuf in=(ByteBuf)msg;
        ByteBuf temp=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher,in,temp);//update 解密
        out.writeBytes(temp);
        ctx.write(out);
    }

}
