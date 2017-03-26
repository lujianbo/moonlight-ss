package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCipher;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksCrypto;
import com.lujianbo.app.shadowsocks.common.utils.BytebufCipherUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

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
                byte[] ivbytes = new byte[ivLength];
                in.readBytes(ivbytes);
                this.decryptCipher = new ShadowSocksCipher(ivbytes, crypto, password, false);
            }
        }
        int length = in.readableBytes();
        if (length > 0) {
            ByteBuf out = ctx.alloc().buffer();
            BytebufCipherUtil.update(decryptCipher, in, out);//update decode
            ctx.fireChannelRead(out);
            ReferenceCountUtil.release(in);
        }
    }

}
