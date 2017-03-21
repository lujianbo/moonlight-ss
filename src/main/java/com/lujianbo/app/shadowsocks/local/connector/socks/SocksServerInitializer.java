package com.lujianbo.app.shadowsocks.local.connector.socks;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;

public class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        //Socks request decode it will remove itself
        p.addLast(new SocksInitRequestDecoder());
        //socks encode
        p.addLast(new SocksMessageEncoder());
        //数据处理
        p.addLast(new SocksRequestHandler());

    }
}
