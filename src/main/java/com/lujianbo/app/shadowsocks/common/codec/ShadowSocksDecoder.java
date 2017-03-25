package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCipher;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCrypto;
import com.lujianbo.app.shadowsocks.common.utils.BytebufCipherUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * IV 作为数据开头的加密处理
 */
public class ShadowSocksDecoder extends ChannelInboundHandlerAdapter {

    private final ShadowSocksCrypto crypto;
    private transient ShadowSocksCipher decryptCipher;
    private String password;
    private boolean readIV = false;
    private int ivLength = 16;

    public ShadowSocksDecoder(String password, ShadowSocksCrypto crypto) {
        this.password = password;
        this.ivLength = crypto.getIvLength();
        this.crypto = crypto;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (!readIV) {
            if (in.readableBytes() < ivLength) {
                return;
            } else {
                readIV = true;
                ByteBuf iv = in.readBytes(ivLength);
                byte[] ivbytes = new byte[ivLength];
                iv.getBytes(0, ivbytes);
                this.decryptCipher = new ShadowSocksCipher(ivbytes, crypto, password, false);
            }
        }
        int length = in.readableBytes();
        if (length > 0) {
            ByteBuf en = in.readBytes(length);
            ByteBuf real = ctx.alloc().buffer();
            ByteBuf temp = ctx.alloc().buffer(en.capacity());
            BytebufCipherUtil.update(decryptCipher, en, temp);//update decode
            real.writeBytes(temp);
            ctx.fireChannelRead(real);
        }
    }

}
