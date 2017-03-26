package com.lujianbo.app.shadowsocks.common.handler;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksCodec;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequestEncoder;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by jianbo on 2017/3/25.
 */
public class ShadowSocksClientInitializer extends ChannelInitializer<SocketChannel> {

    private ShadowSocksContext context;

    public ShadowSocksClientInitializer(ShadowSocksContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ShadowSocksCodec(context));
        ch.pipeline().addLast(new ShadowSocksRequestEncoder());
    }
}
