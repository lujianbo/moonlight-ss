package com.lujianbo.app.shadowsocks.server.handler;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksCodec;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequestDecoder;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ShadowSocksServerInitializer extends ChannelInitializer<SocketChannel> {

    private ShadowSocksContext context;
    public ShadowSocksServerInitializer(ShadowSocksContext context){
        this.context=context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // add codec
        ch.pipeline().addLast(new ShadowSocksCodec(context));
        //解码地址部分
        ch.pipeline().addLast(new ShadowSocksRequestDecoder());
        //处理解密后的数据流量
        ch.pipeline().addLast(new ShadowSocksRequestHandler());
    }
}
