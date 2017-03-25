package com.lujianbo.app.shadowsocks.server.handler;

import com.lujianbo.app.shadowsocks.common.codec.SSRequestDecoder;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksCodec;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;

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
        //ch.pipeline().addLast(new ShadowSocksRequestDecoder());

        ch.pipeline().addLast(new SSRequestDecoder());
        ch.pipeline().addLast(new LoggingHandler());
        ch.pipeline().addLast(new ShadowSocksRequestHandler());
        //处理解密后的数据流量
        //ch.pipeline().addLast(new SSRequestDecoder());
    }
}
