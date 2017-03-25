package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCipher;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCrypto;
import com.lujianbo.app.shadowsocks.common.utils.BytebufCipherUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;


/**
 * 对流过的数据都进行cipher 的 update操作
 */
public class ShadowSocksEncoder extends ChannelOutboundHandlerAdapter {

    private transient ShadowSocksCipher encryptCipher;

    private boolean iv_sent = false;

    private ShadowSocksCrypto crypto;

    public ShadowSocksEncoder(String password, ShadowSocksCrypto crypto) {
        this.crypto = crypto;
        encryptCipher = new ShadowSocksCipher(crypto.generatorIvParameter(), crypto, password, true);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        if (!iv_sent) {
            iv_sent = true;
            out.writeBytes(encryptCipher.getIv());
        }
        ByteBuf in = (ByteBuf) msg;
        ByteBuf temp = ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher, in, temp);//update 解密
        out.writeBytes(temp);
        ctx.writeAndFlush(out);
    }

}
