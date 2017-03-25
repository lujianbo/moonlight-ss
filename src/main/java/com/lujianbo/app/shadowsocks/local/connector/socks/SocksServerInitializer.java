package com.lujianbo.app.shadowsocks.local.connector.socks;

import com.lujianbo.app.shadowsocks.local.handler.ShadowSocksConnectHandler;
import com.lujianbo.app.shadowsocks.local.manager.SSServerInstance;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;
import io.netty.handler.logging.LoggingHandler;

public class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    private SSServerInstance instance;

    public SocksServerInitializer(SSServerInstance instance){
        this.instance=instance;
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
