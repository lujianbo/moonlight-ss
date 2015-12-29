package io.xdd.blackscience.socksserver.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.xdd.blackscience.socksserver.core.handler.codec.ShadowSocksDecoder;
import io.xdd.blackscience.socksserver.core.handler.codec.ShadowSocksEncoder;

public class ShadowSocksServerConnectInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();

        p.addLast(new ShadowSocksDecoder());
        p.addLast(new ShadowSocksEncoder());
        p.addLast(new ShadowSocksServerConnectHandler());
    }
}
