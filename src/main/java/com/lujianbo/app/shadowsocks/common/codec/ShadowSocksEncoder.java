package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCipher;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCrypto;
import com.lujianbo.app.shadowsocks.common.utils.BytebufCipherUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;


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
        if (!iv_sent) {
            iv_sent = true;
            ByteBuf iv = ctx.alloc().buffer();
            iv.writeBytes(encryptCipher.getIv());
            ctx.write(iv);
        }
        ByteBuf in = (ByteBuf) msg;
        ByteBuf out = ctx.alloc().buffer();
        BytebufCipherUtil.update(encryptCipher, in, out);//update 解密
        ctx.write(out);
        ReferenceCountUtil.release(msg);
    }

}
