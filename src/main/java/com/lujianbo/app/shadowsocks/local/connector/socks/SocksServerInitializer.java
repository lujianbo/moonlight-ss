package com.lujianbo.app.shadowsocks.local.connector.socks;

import com.lujianbo.app.shadowsocks.local.manager.ShadowSocksServerInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;

public class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    private ShadowSocksServerInfo instance;

    public SocksServerInitializer(ShadowSocksServerInfo instance) {
        this.instance = instance;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        //Socks request decode it will remove itself
        p.addLast(new SocksInitRequestDecoder());
        //socks encode
        p.addLast(new SocksMessageEncoder());
        //数据处理
        p.addLast(new SocksRequestHandler());
        //走向代理连接
        p.addLast(new ShadowSocksConnectHandler(instance));

    }
}
